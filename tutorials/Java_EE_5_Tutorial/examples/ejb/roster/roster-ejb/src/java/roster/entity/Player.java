/*
 * Copyright (c) 2006 Sun Microsystems, Inc.  All rights reserved.  U.S.
 * Government Rights - Commercial software.  Government users are subject
 * to the Sun Microsystems, Inc. standard license agreement and
 * applicable provisions of the FAR and its supplements.  Use is subject
 * to license terms.
 *
 * This distribution may include materials developed by third parties.
 * Sun, Sun Microsystems, the Sun logo, Java and J2EE are trademarks
 * or registered trademarks of Sun Microsystems, Inc. in the U.S. and
 * other countries.
 *
 * Copyright (c) 2006 Sun Microsystems, Inc. Tous droits reserves.
 *
 * Droits du gouvernement americain, utilisateurs gouvernementaux - logiciel
 * commercial. Les utilisateurs gouvernementaux sont soumis au contrat de
 * licence standard de Sun Microsystems, Inc., ainsi qu'aux dispositions
 * en vigueur de la FAR (Federal Acquisition Regulations) et des
 * supplements a celles-ci.  Distribue par des licences qui en
 * restreignent l'utilisation.
 *
 * Cette distribution peut comprendre des composants developpes par des
 * tierces parties. Sun, Sun Microsystems, le logo Sun, Java et J2EE
 * sont des marques de fabrique ou des marques deposees de Sun
 * Microsystems, Inc. aux Etats-Unis et dans d'autres pays.
 */


package roster.entity;

import java.util.Collection;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name = "EJB_ROSTER_PLAYER")
@NamedQueries({
    @NamedQuery(name = "roster.entity.Player.findAllPlayers",query = "SELECT p FROM Player p")
    , @NamedQuery(name = "roster.entity.Player.findPlayersByCity", query = "SELECT DISTINCT p "
    + "FROM Player p, " + "IN (p.teams) t " + "WHERE t.city = :city")
    , @NamedQuery(name = "roster.entity.Player.findPlayersByHigherSalary", query = "SELECT DISTINCT p1 "
    + "FROM Player p1, Player p2 " + "WHERE p1.salary > p2.salary AND "
    + "p2.name = :name")
    , @NamedQuery(name = "roster.entity.Player.findPlayersByLeague", query = "SELECT DISTINCT p "
    + "FROM Player p, " + "IN (p.teams) t " + "WHERE t.league = :league")
    , @NamedQuery(name = "roster.entity.Player.findPlayersByPosition", query = "SELECT DISTINCT p "
    + "FROM Player p " + "WHERE p.position = :position")
    , @NamedQuery(name = "roster.entity.Player.findPlayersByPositionAndName", query = "SELECT DISTINCT p "
    + "FROM Player p " + "WHERE p.position = :position AND p.name = :name")
    , @NamedQuery(name = "roster.entity.Player.findPlayersBySalaryRange", query = "SELECT DISTINCT p "
    + "FROM Player p "
    + "WHERE p.salary BETWEEN :lowerSalary AND :higherSalary")
    , @NamedQuery(name = "roster.entity.Player.findPlayersBySport", query = "SELECT DISTINCT p "
    + "FROM Player p, " + "IN (p.teams) t " + "WHERE t.league.sport = :sport")
    , @NamedQuery(name = "roster.entity.Player.findPlayersNotOnTeam", query = "SELECT p "
    + "FROM Player p " + "WHERE p.teams IS EMPTY")
    , @NamedQuery(name = "roster.entity.Player.findLeaguesByPlayer", query = "SELECT t.league "
    + "FROM Player p, IN (p.teams) t " + "WHERE p = :player")
    , @NamedQuery(name = "roster.entity.Player.findSportsByPlayer", query = "SELECT DISTINCT t.league.sport "
    + "FROM Player p, IN (p.teams) t " + "WHERE p.id = :playerId")
})
public class Player implements java.io.Serializable {
    private Collection<Team> teams;
    private String id;
    private String name;
    private String position;
    private double salary;

    /** Creates a new instance of Player */
    public Player() {
    }

    public Player(
        String id,
        String name,
        String position,
        double salary) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.salary = salary;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @ManyToMany(cascade = REMOVE, mappedBy = "players")
    public Collection<Team> getTeams() {
        return teams;
    }

    public void setTeams(Collection<Team> teams) {
        this.teams = teams;
    }
}
