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

import arden.CommandLineOptions;
import arden.MainClass;
import arden.compiler.CompiledMlm;
import arden.compiler.Compiler;
import arden.compiler.CompilerException;

/**
 * Implements the search strategy to find a matching MLM by name and
 * institution from a searchpath (classpath + working directory)
 */
public class BaseExecutionContext extends ExecutionContext {
	List<URL> mlmSearchPath = new LinkedList<URL>();
	Map<String, ArdenRunnable> moduleList = new HashMap<String, ArdenRunnable>();

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
	public void callWithDelay(ArdenRunnable mlm, ArdenValue[] arguments, ArdenValue delay) {
		// print delay and run now
		System.out.println("delay: " + delay.toString());
		try {
			mlm.run(this, arguments);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
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
}
