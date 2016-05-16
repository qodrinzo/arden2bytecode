package arden.tests.specification.StructureSlots;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;

public class EvokeSlotTest extends SpecificationTest {
	private final String event_data = new ArdenCodeBuilder()
			.addData("test_event := EVENT {" + getMappings().getEventMapping() + "};")
			.toString();

	@Test
	public void testTimeOfEvent() throws Exception {
		
		String othermlm = new ArdenCodeBuilder(event_data)
				.replaceSlotContent("mlmname:", "other_mlm")
				.addEvoke("test_event;")
				.addAction("RETURN (TIME OF test_event = eventtime);")
				.toString();
		String eventtime = new ArdenCodeBuilder(event_data)
				.addMlm(othermlm)
				.clearSlotContent("logic:")
				.addLogic("result := CALL test_event;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN result;")
				.toString();
		assertReturns(eventtime, "TRUE");
	}

	@Test
	public void testSimpleTriggerStatement() throws Exception {
		String any = new ArdenCodeBuilder()
				.addData("test_event1 := EVENT {" + getMappings().getEventMapping() + "};")
				.addData("test_event2 := EVENT {" + getMappings().getEventMapping() + "};")
				.addData("test_event3 := EVENT {" + getMappings().getEventMapping() + "};")
				.addEvoke("test_event1 OR ANY OF(test_event2, test_event3);")
				.toString();
		assertValid(any);
		
		String invalidEvent = new ArdenCodeBuilder()
				.addData("a := 5;")
				.addEvoke("a;")
				.toString();
		assertInvalid(invalidEvent);
		
		String othermlm = new ArdenCodeBuilder()
				.replaceSlotContent("mlmname:", "other_mlm")
				.addEvoke("CALL;")
				.toString();
		String call = new ArdenCodeBuilder()
				.addMlm(othermlm)
				.addData("othermlm := MLM 'other_mlm';")
				.addAction("CALL othermlm;")
				.toString();
		assertValid(call);
		
		// TODO test logic
	}

	@Test
	public void testDelayedTriggerStatement() throws Exception {
		String after = new ArdenCodeBuilder(event_data).addEvoke("10 SECONDS AFTER TIME OF test_event;").toString();
		assertValid(after);
		
		String before = new ArdenCodeBuilder(event_data).addEvoke("10 SECONDS BEFORE TIME OF test_event;").toString();
		assertInvalid(before);
		
		String time = new ArdenCodeBuilder(event_data).addEvoke("2016-01-01T12:00:00; 2016-01-02T12:00:00;").toString();
		assertValid(time);
		
		String date = new ArdenCodeBuilder(event_data).addEvoke("2016-01-01;").toString();
		assertValid(date);
		
		String eventTime = new ArdenCodeBuilder(event_data).addEvoke("TIME OF test_event;").toString();
		assertValid(eventTime);
		
		String duration = new ArdenCodeBuilder(event_data).addEvoke("5 SECONDS;").toString();
		assertInvalid(duration);
	}

	@Test
	public void testPeriodicTriggerStatement() throws Exception {
		String simple = new ArdenCodeBuilder(event_data).addEvoke("EVERY 15 SECONDS FOR 1 HOUR STARTING TIME test_event;").toString();
		assertValid(simple);
		
		String complex  = new ArdenCodeBuilder(event_data).addEvoke("EVERY 2 HOURS FOR 1 DAY STARTING 5 HOURS AFTER THE TIME OF test_event;").toString();
		assertValid(complex);
		
		String condition = new ArdenCodeBuilder(event_data)
				.addData("a := 0;")
				.addEvoke("EVERY 1 WEEK FOR 6 MONTHS STARTING 1990-01-01T12:00:00 UNTIL a > 5;").toString();
		assertValid(condition);
	}

}
