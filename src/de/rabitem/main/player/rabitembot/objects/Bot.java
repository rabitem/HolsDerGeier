package de.rabitem.main.player.rabitembot.objects;

import de.rabitem.main.player.Player;
import de.rabitem.main.player.instances.Felix;

import java.sql.Timestamp;
import java.util.Objects;

public class Bot implements MySqlUtil{

    private int id;
    private Player owner;
    private Timestamp lastPlayedAgainst;
    private int wins;
    private int loses;
    private int draws;

    public Bot(Player owner) {
        this.owner = owner;
    }

    public Bot(int id, Player owner, Timestamp lastPlayedAgainst, int wins, int loses, int draws) {
        this.id = id;
        this.owner = owner;
        this.lastPlayedAgainst = lastPlayedAgainst;
        this.wins = wins;
        this.loses = loses;
        this.draws = draws;
    }

    public Player getOwner() {
        return owner;
    }

    private void setup() {

    }

    @Override
    public boolean existsInDatabase() {
        return this.getAttribute("count(*) as count", "count", "bot", Integer.class, this.owner) == 1;
    }

    @Override
    public void pushDatabase() {
        if (Objects.isNull(this.owner) || this.existsInDatabase()) {
            System.out.println("[ERROR] Couldn't push Bot to Database");
            return;
        }
        String query = "insert into holsdergeier.bot(Name)\n" +
                "values(\"" + Objects.requireNonNull(this.owner.getName()) + "\");";
        Felix.mySql.update(query);
    }

    public void setLastPlayed(Timestamp timestamp) {
        if (Objects.isNull(this.owner)) {
            System.out.println("[ERROR] Couldn't push Bot to Database");
            return;
        }
        String query = "update holsdergeier.bot " +
                "set LastPlayedAgainst = \"" + timestamp +  "\" " +
                "where Name = \"" + this.owner.getName() + "\";";
        Felix.mySql.update(query);
    }

    public void addWin() {
        this.updateAttribute("Wins", "Wins + 1","bot", this.owner);
    }

    public void addLose() {
        this.updateAttribute("Loses", "Loses + 1","bot", this.owner);
    }

    public void addDraw() {
        this.updateAttribute("Draws", "Draws + 1","bot", this.owner);
    }

    public int getWins() {
        return this.getAttribute("Wins", "bot", Integer.class, this.owner);
    }

    public int getLoses() {
        return this.getAttribute("Loses", "bot", Integer.class, this.owner);
    }

    public int getDraws() {
        return this.getAttribute("Draws", "bot", Integer.class, this.owner);
    }

    public int getId() {
        return this.getAttribute("idBot", "bot", Integer.class, this.owner);
    }

    public int getTotalRoundsPlayedAgainst() {
        return getWins() + getLoses() + getDraws();
    }
}
