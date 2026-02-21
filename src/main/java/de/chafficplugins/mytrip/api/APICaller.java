package de.chafficplugins.mytrip.api;

import java.util.ArrayList;

public class APICaller {
    public static final ArrayList<DrugToolAPIEvents> DRUG_TOOL_API_EVENTS = new ArrayList<>();
    public static final ArrayList<DrugAPIEvents> DRUG_API_EVENTS = new ArrayList<>();
    public static final ArrayList<DrugSetAPIEvents> DRUG_SET_API_EVENTS = new ArrayList<>();

    public static void registerEvent(MyTripEvent event) {
        if(event instanceof DrugToolAPIEvents) {
            DRUG_TOOL_API_EVENTS.add((DrugToolAPIEvents) event);
        } else if(event instanceof DrugAPIEvents) {
            DRUG_API_EVENTS.add((DrugAPIEvents) event);
        } else if(event instanceof DrugSetAPIEvents) {
            DRUG_SET_API_EVENTS.add((DrugSetAPIEvents) event);
        } else {
            throw new IllegalArgumentException("Event is not supported!");
        }
    }

    public static void unregisterEvent(MyTripEvent event) {
        if(event instanceof DrugToolAPIEvents) {
            DRUG_TOOL_API_EVENTS.remove(event);
        } else if(event instanceof DrugAPIEvents) {
            DRUG_API_EVENTS.remove(event);
        } else if(event instanceof DrugSetAPIEvents) {
            DRUG_SET_API_EVENTS.remove(event);
        }
    }
}
