package src.practiceproblems.agentrating.test;

import src.practiceproblems.agentrating.Agent;
import src.practiceproblems.agentrating.AgentNotFoundException;
import src.practiceproblems.agentrating.AgentService;

import org.junit.jupiter.api.*;                // JUnit 5 annotations & lifecycle

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*; // Assertions (assertEquals, assertThrows, etc.)

public class AgentServiceTest {
    private AgentService agentService;


    @BeforeEach
    void setup(){
        agentService = new AgentService();
    }

    @Test
    void shouldAddAgentSuccessfully() {
        agentService.addAgent(1, "John");
        Agent agent = agentService.getAgentById(1);
        assertEquals(1, agent.getId());
        assertEquals("John", agent.getName());
    }

    @Test
    void shouldThrowExceptionWhenAgentNotFound() {
        assertThrows(AgentNotFoundException.class, () -> agentService.getAgentById(1));
    }

    @Test
    void shouldUpdateAgentSuccessfully() {
        agentService.addAgent(1, "John");
        Agent agent = agentService.getAgentById(1);
        assertEquals(1, agent.getId());
        assertEquals("John", agent.getName());

        agentService.addAgent(1, "Pavan");
        agent = agentService.getAgentById(1);
        assertEquals("Pavan", agent.getName());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentAgent() {
        assertThrows(AgentNotFoundException.class, () -> agentService.addRating(1, 4.3, 2025, 12));
    }

    @Test
    void testAddRating(){
        agentService.addAgent(1, "Pavan");
        agentService.addRating(1, 4.5, 2025, 1);
        agentService.addRating(1, 4, 2025, 1);

        Agent agent = agentService.getAgentById(1);
        assertEquals(4.25, agent.getAverageRating());

        assertEquals(2, agent.getRatingCount());

        agentService.addRating(1, 5, 2025, 2);
        assertEquals(4.5, agent.getAverageRating());
        assertEquals(3, agent.getRatingCount());
        assertEquals(4.25, agent.getAverageRatingsByMonth(YearMonth.of(2025, 1)));

        agentService.addAgent(2, "Ramanan");
        agentService.addRating(2, 5, 2025, 1);
        agentService.addRating(2, 5, 2025, 1);
        agentService.addRating(2, 1, 2025, 2);

        List<Agent> expectedTotal = new ArrayList<>(Arrays.asList(new Agent(1, "Pavan"), new Agent(2, "Ramanan")));
        assertEquals(expectedTotal.reversed(), agentService.getAgentsByAverageMonthlyRating(2025, 1));
        assertEquals(expectedTotal, agentService.getAgentsByAverageRating());

    }

}
