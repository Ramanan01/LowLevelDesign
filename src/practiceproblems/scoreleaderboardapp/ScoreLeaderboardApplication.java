package src.practiceproblems.scoreleaderboardapp;


import java.util.*;

//
//interface PointsObserver {
//    public void updateScore(int delta);
//}

class Player {
    String playerId;
    String name;
    int score;
    Set<User> users;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        playerId = UUID.randomUUID().toString();
        users = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public Set<User> getUsers() {
        return Collections.unmodifiableSet(users);
    }

    public void updateScore(int newScore) {
        int delta = newScore - score;
        this.score = newScore;

        for(User user: users) {
            user.updateScore(delta);
        }
    }
}

class User {

    Set<Player> team;
    String userName;
    String userId;
    int teamScore;

    public User(Set<Player> team, String userName) {
        this.team = team;
        this.userName = userName;
        this.userId = UUID.randomUUID().toString();
        teamScore = 0;
        for(Player p: team) {
            p.addUser(this);
            teamScore += p.getScore();
        }
    }

    public void updateScore(int delta) {
        this.teamScore += delta;
    }

    public Set<Player> getTeam() {
        return team;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public int getTeamScore() {
        return teamScore;
    }
}

class ScoreLeaderboard {
    Map<String, User> users;
    Map<String, Player> players;
    Map<Integer, Set<User>> leaderboard;

    public ScoreLeaderboard(String[] playersList) {
        users = new HashMap<>();
        players = new HashMap<>();

        leaderboard = new TreeMap<>((a, b) -> Integer.compare(b, a));

        for(String playerName: playersList) {
            Player player = new Player(playerName);
            players.put(player.getPlayerId(), player);
        }
    }

    public void addUser(String userName, Set<Player> team) {
        User user = new User(team, userName);
        users.put(user.getUserId(), user);
        addToLeaderboard(user);
    }

    public void updateScore(String playerId, int newScore) {
        if(!players.containsKey(playerId)) {
            throw new IllegalArgumentException("Invalid player id");
        }

        Player player = players.get(playerId);
        Set<User> impactedUsers = player.getUsers();

        for(User u: impactedUsers) {
            removeFromLeaderboard(u);
        }

        player.updateScore(newScore);

        for(User u: impactedUsers) {
            addToLeaderboard(u);
        }
    }

    public List<User> getTopK(int k) {
        List<User> topKUsers = new ArrayList<>();
        for(Integer score : leaderboard.keySet()) {
            for(User user: leaderboard.get(score)) {
                topKUsers.add(user);
                if(topKUsers.size() == k) {
                    break;
                }
            }
            if(topKUsers.size() == k) {
                break;
            }
        }

        return topKUsers;
    }

    private void removeFromLeaderboard(User user) {
        Set<User> bucket = leaderboard.get(user.getTeamScore());

        if(bucket == null)
            return;

        bucket.remove(user);

        if(bucket.isEmpty()) {
            leaderboard.remove(user.getTeamScore());
        }
    }

    private void addToLeaderboard(User user) {
        leaderboard.computeIfAbsent(user.getTeamScore(), x-> new LinkedHashSet<>()).add(user);
    }
}

public class ScoreLeaderboardApplication {
}
