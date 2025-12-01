package Dto;

import jakarta.persistence.*;

import java.sql.Time;

@Entity
@Table(name = "Production_DayWise")
public class OpcEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long sno;
    private int target;
    private int rooltarget;
    private int actual;
    private int takt;
    private long uptime;
    private long downtime;
    private Time rowinserttime;

    public long getSno() {
        return sno;
    }

    public void setSno(long sno) {
        this.sno = sno;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getRooltarget() {
        return rooltarget;
    }

    public void setRooltarget(int rooltarget) {
        this.rooltarget = rooltarget;
    }

    public int getActual() {
        return actual;
    }

    public void setActual(int actual) {
        this.actual = actual;
    }

    public int getTakt() {
        return takt;
    }

    public void setTakt(int takt) {
        this.takt = takt;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public long getDowntime() {
        return downtime;
    }

    public void setDowntime(long downtime) {
        this.downtime = downtime;
    }

    public Time getRowinserttime() {
        return rowinserttime;
    }

    public void setRowinserttime(Time rowinserttime) {
        this.rowinserttime = rowinserttime;
    }
}