package de.chafficplugins.mytrip.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class APICallerTest {

    @BeforeEach
    void setUp() {
        APICaller.DRUG_TOOL_API_EVENTS.clear();
        APICaller.DRUG_API_EVENTS.clear();
        APICaller.DRUG_SET_API_EVENTS.clear();
    }

    @Test
    void registerEvent_drugToolEvent_addsToCorrectList() {
        DrugToolAPIEvents event = new DrugToolAPIEvents() {};
        APICaller.registerEvent(event);
        assertEquals(1, APICaller.DRUG_TOOL_API_EVENTS.size());
        assertTrue(APICaller.DRUG_TOOL_API_EVENTS.contains(event));
        assertEquals(0, APICaller.DRUG_API_EVENTS.size());
        assertEquals(0, APICaller.DRUG_SET_API_EVENTS.size());
    }

    @Test
    void registerEvent_drugEvent_addsToCorrectList() {
        DrugAPIEvents event = new DrugAPIEvents();
        APICaller.registerEvent(event);
        assertEquals(1, APICaller.DRUG_API_EVENTS.size());
        assertTrue(APICaller.DRUG_API_EVENTS.contains(event));
        assertEquals(0, APICaller.DRUG_TOOL_API_EVENTS.size());
        assertEquals(0, APICaller.DRUG_SET_API_EVENTS.size());
    }

    @Test
    void registerEvent_drugSetEvent_addsToCorrectList() {
        DrugSetAPIEvents event = new DrugSetAPIEvents();
        APICaller.registerEvent(event);
        assertEquals(1, APICaller.DRUG_SET_API_EVENTS.size());
        assertTrue(APICaller.DRUG_SET_API_EVENTS.contains(event));
        assertEquals(0, APICaller.DRUG_TOOL_API_EVENTS.size());
        assertEquals(0, APICaller.DRUG_API_EVENTS.size());
    }

    @Test
    void registerEvent_unsupportedEvent_throwsException() {
        MyTripEvent event = new MyTripEvent();
        assertThrows(IllegalArgumentException.class, () -> APICaller.registerEvent(event));
    }

    @Test
    void unregisterEvent_removesFromList() {
        DrugToolAPIEvents event = new DrugToolAPIEvents() {};
        APICaller.registerEvent(event);
        assertEquals(1, APICaller.DRUG_TOOL_API_EVENTS.size());

        APICaller.unregisterEvent(event);
        assertEquals(0, APICaller.DRUG_TOOL_API_EVENTS.size());
    }

    @Test
    void registerMultipleEvents_allAdded() {
        DrugToolAPIEvents toolEvent1 = new DrugToolAPIEvents() {};
        DrugToolAPIEvents toolEvent2 = new DrugToolAPIEvents() {};
        DrugAPIEvents drugEvent = new DrugAPIEvents();

        APICaller.registerEvent(toolEvent1);
        APICaller.registerEvent(toolEvent2);
        APICaller.registerEvent(drugEvent);

        assertEquals(2, APICaller.DRUG_TOOL_API_EVENTS.size());
        assertEquals(1, APICaller.DRUG_API_EVENTS.size());
    }

    // --- Bug-catching tests: unregisterEvent is incomplete ---

    @Test
    void unregisterEvent_drugApiEvent_doesNotRemove() {
        // BUG: unregisterEvent only handles DrugToolAPIEvents.
        // DrugAPIEvents cannot be unregistered — they remain in the list forever.
        DrugAPIEvents event = new DrugAPIEvents();
        APICaller.registerEvent(event);
        assertEquals(1, APICaller.DRUG_API_EVENTS.size());

        APICaller.unregisterEvent(event);
        // BUG: event is NOT removed because unregisterEvent doesn't handle DrugAPIEvents
        assertEquals(1, APICaller.DRUG_API_EVENTS.size(),
                "BUG: DrugAPIEvents cannot be unregistered — unregisterEvent only handles DrugToolAPIEvents");
    }

    @Test
    void unregisterEvent_drugSetApiEvent_doesNotRemove() {
        // BUG: Same issue — DrugSetAPIEvents cannot be unregistered.
        DrugSetAPIEvents event = new DrugSetAPIEvents();
        APICaller.registerEvent(event);
        assertEquals(1, APICaller.DRUG_SET_API_EVENTS.size());

        APICaller.unregisterEvent(event);
        // BUG: event is NOT removed
        assertEquals(1, APICaller.DRUG_SET_API_EVENTS.size(),
                "BUG: DrugSetAPIEvents cannot be unregistered — unregisterEvent only handles DrugToolAPIEvents");
    }

    @Test
    void unregisterEvent_drugToolEvent_actuallyRemoves() {
        // DrugToolAPIEvents is the only type that CAN be unregistered (confirms the asymmetry)
        DrugToolAPIEvents event = new DrugToolAPIEvents() {};
        APICaller.registerEvent(event);
        assertEquals(1, APICaller.DRUG_TOOL_API_EVENTS.size());

        APICaller.unregisterEvent(event);
        assertEquals(0, APICaller.DRUG_TOOL_API_EVENTS.size(),
                "DrugToolAPIEvents should be removable (this is the only type that works)");
    }

    @Test
    void unregisterEvent_nonRegisteredEvent_doesNotThrow() {
        DrugToolAPIEvents event = new DrugToolAPIEvents() {};
        assertDoesNotThrow(() -> APICaller.unregisterEvent(event));
    }

    @Test
    void registerEvent_sameEventTwice_addsDuplicates() {
        // No deduplication — registering the same event twice adds it twice
        DrugAPIEvents event = new DrugAPIEvents();
        APICaller.registerEvent(event);
        APICaller.registerEvent(event);
        assertEquals(2, APICaller.DRUG_API_EVENTS.size(),
                "Duplicate registrations are not prevented — same event added twice");
    }
}
