package arden.runtime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import arden.CommandLineOptions;
import arden.EventEngine;
import arden.MainClass;
import arden.compiler.CompiledMlm;
import arden.compiler.Compiler;
import arden.compiler.CompilerException;
import arden.runtime.events.EvokeEvent;

/**
 * <p>
 * Implements the search strategy to find a matching MLM by name and institution
 * from a local searchpath (classpath + working directory).
 * </p>
 * <p>
 * Also allows calling other MLMs in the action slot (directly or via event). If
 * an {@link EventEngine} is not set, cyclic or delayed triggers are not run and
 * delayed calls are run immediately.
 * </p>
 */
public class BaseExecutionContext extends ExecutionContext {
	private List<URL> mlmSearchPath = new LinkedList<URL>();
	private Map<String, ArdenRunnable> moduleList = new HashMap<String, ArdenRunnable>();
	private EventEngine engine;

	public BaseExecutionContext(URL[] mlmSearchPath) {
		setURLs(mlmSearchPath);
	}

	public BaseExecutionContext(CommandLineOptions options) {
		try {
			// look for MLMs in classpath + working directory
			addURL(new File(".").toURI().toURL());
			if (options.isClasspath()) {
				String[] paths = options.getClasspath().split(File.pathSeparator);
				for (String path : paths) {
					URL url = new File(path).toURI().toURL();
					addURL(url);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void addURL(URL url) {
		mlmSearchPath.add(url);
	}

	public void setURLs(URL[] urls) {
		mlmSearchPath.clear();
		if (urls != null) {
			mlmSearchPath.addAll(Arrays.asList(urls));
		}
	}

	@Override
	public ArdenRunnable findModule(String name, String institution) {
		if (!name.matches("[a-zA-Z0-9\\-_]+")) {
			throw new RuntimeException("Malformed module name: " + name);
		}

		ArdenRunnable fromlist = moduleList.get(name.toLowerCase());
		if (fromlist != null) {
			return fromlist;
		}

		URLClassLoader loader = new URLClassLoader(mlmSearchPath.toArray(new URL[] {}));
		InputStream in = loader.getResourceAsStream(name + ".class");
		if (in != null) {
			try {
				ArdenRunnable module = new CompiledMlm(in, name);
				moduleList.put(name.toLowerCase(), module);
				return module;
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					loader.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		}
		in = loader.getResourceAsStream(name + MainClass.MLM_FILE_EXTENSION);
		Compiler compiler = new Compiler();
		MedicalLogicModule mlm;
		try {
			mlm = compiler.compile(new InputStreamReader(in, "UTF-8")).get(0);
		} catch (CompilerException | IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				loader.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		moduleList.put(name.toLowerCase(), mlm);
		return mlm;
	}

	public void setEngine(EventEngine engine) {
		this.engine = engine;
	}

	@Override
	public void callWithDelay(ArdenRunnable mlm, ArdenValue[] arguments, ArdenValue delay) {
		if (engine != null) {
			// get delay
			if (!(delay instanceof ArdenDuration)) {
				System.err.println(delay.getClass().getSimpleName());
				throw new RuntimeException("Delay must be a duration");
			}
			ArdenDuration delayDuration = (ArdenDuration) delay;
			long delayMillis = Math.round(delayDuration.toSeconds() * 1000);

			// use urgency as priority
			int urgency = 50;
			if (mlm instanceof MedicalLogicModule) {
				MedicalLogicModule module = (MedicalLogicModule) mlm;
				urgency = (int) Math.round(module.getUrgency());
			}

			// run on engine
			engine.callWithDelay(mlm, arguments, urgency, delayMillis);
		} else {
			// print delay and run now
			System.out.println("delay (skipped): " + delay.toString());
			try {
				mlm.run(this, arguments);
			} catch (InvocationTargetException e) {
				System.err.println("Could not run MLM:");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void callEvent(String mapping, ArdenTime eventTime) {
		if (engine != null) {
			// run on engine
			engine.callEvent(mapping, eventTime);
		} else {
			// run MLMs for event now
			System.out.println("event: " + mapping);
			for (Entry<String, ArdenRunnable> entry : moduleList.entrySet()) {
				ArdenRunnable runnable = entry.getValue();
				if (runnable instanceof MedicalLogicModule) {
					MedicalLogicModule mlm = (MedicalLogicModule) runnable;

					try {
						EvokeEvent evokeEvent = mlm.getEvoke(this, null);
						if (evokeEvent.runOnEvent(mapping, this)) {
							super.eventTime = eventTime;
							mlm.run(this, null);
						}
					} catch (InvocationTargetException e) {
						System.err.println("Could not run MLM:");
						e.printStackTrace();
					}

				}

			}
		}
	}
}
