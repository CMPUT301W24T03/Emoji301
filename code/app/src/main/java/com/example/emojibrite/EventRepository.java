package com.example.emojibrite;

import java.util.HashMap;
import java.util.Map;

public class EventRepository {
    private static EventRepository instance;
    private final Map<String, Event> eventMap;

    private EventRepository() {
        eventMap = new HashMap<>();
    }

    public static synchronized EventRepository getInstance() {
        if (instance == null) {
            instance = new EventRepository();
        }
        return instance;
    }

    public void addEvent(Event event) {
        eventMap.put(event.getId(), event);
    }

    public Event getEventById(String id) {
        return eventMap.get(id);
    }
}