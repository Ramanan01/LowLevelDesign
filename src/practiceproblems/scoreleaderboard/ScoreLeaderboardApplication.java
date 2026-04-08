package src.practiceproblems.scoreleaderboard;

import java.util.*;

interface PointsObserver {
    void updatePoints(int delta);
}

class Player {
    private final String id;
    private int score;
    private final String name;
    private final List<PointsObserver> selectors;

    public Player(String name) {
        id = UUID.randomUUID().toString();
        score = 0;
        this.name = name;
        selectors = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public List<PointsObserver> getSelectors() {
        return selectors;
    }

    public void addSelector(PointsObserver observer){
        selectors.add(observer);
    }

    public void setScore(int newScore) {
        int delta = newScore - score;
        score = newScore;

        for(PointsObserver pointsObserver : selectors){
            pointsObserver.updatePoints(delta);
        }
    }
}

class User implements PointsObserver {
    private final String id;
    private int teamScore;
    private final String name;
    private final Set<Player> team;

    public User(String name, Set<Player> team) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.team = team;
        this.teamScore = 0;
        for(Player player: team) {
            if(player!=null) {
                player.addSelector(this);
                teamScore += player.getScore();
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Player> getTeam() {
        return team;
    }

    public int getTeamScore() {
        return teamScore;
    }

    @Override
    public void updatePoints(int delta) {
        this.teamScore += delta;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof User user) {
            return Objects.equals(user.id, this.id);
        }
        else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}

class LeaderboardService {
    private final Map<String, User> users;
    private final Map<String, Player> players;
    private final TreeSet<User> leaderboard;

    public LeaderboardService() {
        users = new HashMap<>();
        players = new HashMap<>();
        leaderboard = new TreeSet<>((a, b) -> {
            if (a.getTeamScore() != b.getTeamScore()) {
                return Integer.compare(b.getTeamScore(), a.getTeamScore());
            }
            return a.getId().compareTo(b.getId());
        });
    }

    public String addUser(String userName, Set<Player> team) {
        synchronized (leaderboard) {
            if (userName == null || userName.isEmpty()) {
                throw new IllegalArgumentException("Empty username");
            }

            User user = new User(userName, team);

            users.put(user.getId(), user);

            leaderboard.add(user);

            return user.getId();
        }
    }

    public String addPlayer(String playerName) {
        if(playerName==null || playerName.isEmpty()) {
            throw new IllegalArgumentException("Empty username");
        }

        Player player = new Player(playerName);

        players.put(player.getId(), player);

        return player.getId();
    }

    public void pointsUpdate(String playerId, int newPoints) {
        if(playerId==null || playerId.isEmpty()){
            throw new IllegalArgumentException("Empty player id");
        }

        Player player = players.get(playerId);

        if(player==null) {
            throw new IllegalArgumentException("Invalid player id");
        }

        synchronized (leaderboard) {

            List<PointsObserver> usersOfPlayer = player.getSelectors();
            for(PointsObserver pointsObserver: usersOfPlayer) {
                User user = (User) pointsObserver;
                leaderboard.remove(user);

            }

            player.setScore(newPoints);

            for(PointsObserver pointsObserver: usersOfPlayer) {
                User user = (User) pointsObserver;
                leaderboard.add(user);
            }
        }
    }

    public List<User> getLeaderboard(int k) {
        synchronized (leaderboard) {
            List<User> leaders = new ArrayList<>();

            Iterator<User> it = leaderboard.iterator();
            int i = 0;
            while (it.hasNext() && i < k) {
                leaders.add(it.next());
                i++;
            }

            return leaders;
        }
    }
}

public class ScoreLeaderboardApplication {
    public static void main(String[] args) {
        LeaderboardService service = new LeaderboardService();

        // 1. Create some players
        String p1Id = service.addPlayer("Virat Kohli");
        String p2Id = service.addPlayer("MS Dhoni");
        String p3Id = service.addPlayer("Rohit Sharma");

        Player p1 = getPlayerFromService(service, p1Id);
        Player p2 = getPlayerFromService(service, p2Id);
        Player p3 = getPlayerFromService(service, p3Id);

        // 2. Create teams (Users)
        Set<Player> teamA = new HashSet<>(Arrays.asList(p1, p2));
        Set<Player> teamB = new HashSet<>(Arrays.asList(p2, p3));
        Set<Player> teamC = new HashSet<>(Arrays.asList(p1, p3));

        service.addUser("Ramanan_Architects", teamA);
        service.addUser("Sriram_Strikers", teamB);
        service.addUser("Tech_Titans", teamC);

        System.out.println("--- Initial Leaderboard ---");
        printLeaderboard(service.getLeaderboard(3));

        // 3. Simulate Live Match Updates
        System.out.println("\n--- Match Progress: Kohli scores! ---");
        service.pointsUpdate(p1Id, 50);
        printLeaderboard(service.getLeaderboard(3));

        System.out.println("\n--- Match Progress: Dhoni performs! ---");
        service.pointsUpdate(p2Id, 30);
        printLeaderboard(service.getLeaderboard(3));

        System.out.println("\n--- Match Progress: Sharma struggles (Negative points) ---");
        service.pointsUpdate(p3Id, -10);
        printLeaderboard(service.getLeaderboard(3));
    }

    private static void printLeaderboard(List<User> leaders) {
        for (int i = 0; i < leaders.size(); i++) {
            User u = leaders.get(i);
            System.out.println((i + 1) + ". " + u.getName() + " | Score: " + u.getTeamScore());
        }
    }

    // Helper to bypass lack of public player map for testing purposes
    private static Player getPlayerFromService(LeaderboardService service, String id) {
        // In a real app, you'd have a getPlayer method in the service
        try {
            var field = LeaderboardService.class.getDeclaredField("players");
            field.setAccessible(true);
            Map<String, Player> players = (Map<String, Player>) field.get(service);
            return players.get(id);
        } catch (Exception e) {
            return null;
        }
    }
}
