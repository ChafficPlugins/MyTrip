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
    void unregisterEvent_drugApiEvent_removes() {
        DrugAPIEvents event = new DrugAPIEvents();
        APICaller.registerEvent(event);
        assertEquals(1, APICaller.DRUG_API_EVENTS.size());

        APICaller.unregisterEvent(event);
        assertEquals(0, APICaller.DRUG_API_EVENTS.size(),
                "DrugAPIEvents should be unregisterable");
    }

    @Test
    void unregisterEvent_drugSetApiEvent_removes() {
        DrugSetAPIEvents event = new DrugSetAPIEvents();
        APICaller.registerEvent(event);
        assertEquals(1, APICaller.DRUG_SET_API_EVENTS.size());

        APICaller.unregisterEvent(event);
        assertEquals(0, APICaller.DRUG_SET_API_EVENTS.size(),
                "DrugSetAPIEvents should be unregisterable");
    }

    @Test
    void unregisterEvent_drugToolEvent_removes() {
        DrugToolAPIEvents event = new DrugToolAPIEvents() {};
        APICaller.registerEvent(event);
        assertEquals(1, APICaller.DRUG_TOOL_API_EVENTS.size());

        APICaller.unregisterEvent(event);
        assertEquals(0, APICaller.DRUG_TOOL_API_EVENTS.size(),
                "DrugToolAPIEvents should be unregisterable");
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
