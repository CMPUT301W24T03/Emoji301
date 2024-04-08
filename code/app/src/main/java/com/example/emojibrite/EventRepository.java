package com.example.emojibrite;

import java.util.HashMap;
import java.util.Map;

/**
 * event repository
 */
public class EventRepository {
    private static EventRepository instance;
    private final Map<String, Event> eventMap;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the map used to store events.
     */
    private EventRepository() {
        eventMap = new HashMap<>();
    }

    /**
     * Provides the singleton instance of the EventRepository.
     * If the instance does not exist, it creates one; otherwise, it returns the existing instance.
     *
     * @return The singleton instance of EventRepository.
     */
    public static synchronized EventRepository getInstance() {
        if (instance == null) {
            instance = new EventRepository();
        }
        return instance;
    }

    /**
     * Adds an event to the repository.
     * If an event with the same ID already exists, it will be replaced.
     *
     * @param event The {@link Event} object to be added to the repository.
     */
    public void addEvent(Event event) {
        eventMap.put(event.getId(), event);
    }

    /**
     * Retrieves an event by its ID.
     * Returns null if no event with the given ID exists in the repository.
     *
     * @param id The unique identifier of the event to retrieve.
     * @return The {@link Event} object associated with the given ID, or null if not found.
     */
    public Event getEventById(String id) {
        return eventMap.get(id);
    }
}