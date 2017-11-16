package indi.github.icear.simpleclass.data.calendardata.entity;

/**
 * Created by icear on 2017/11/8.
 * 储存Event相关数据
 */

public class EventInfo implements Cloneable {
    private long calendarId;
    private String title;
    private String eventLocation;
    private String description;
    private long dtStart;
    private long dtEnd;
    private String eventTimeZone;
    private String rRule;
    private String rDate;
    private int availability;
    private String organizer;

    @Override
    public EventInfo clone() throws CloneNotSupportedException {
        return (EventInfo) super.clone();
    }

    public long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(long calendarId) {
        this.calendarId = calendarId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDtStart() {
        return dtStart;
    }

    public void setDtStart(long dtStart) {
        this.dtStart = dtStart;
    }

    public long getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(long dtEnd) {
        this.dtEnd = dtEnd;
    }

    public String getEventTimeZone() {
        return eventTimeZone;
    }

    public void setEventTimeZone(String eventTimeZone) {
        this.eventTimeZone = eventTimeZone;
    }

    public String getrRule() {
        return rRule;
    }

    public void setrRule(String rRule) {
        this.rRule = rRule;
    }

    public String getrDate() {
        return rDate;
    }

    public void setrDate(String rDate) {
        this.rDate = rDate;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    @Override
    public String toString() {
        return "EventInfo{" +
                "calendarId=" + calendarId +
                ", title='" + title + '\'' +
                ", eventLocation='" + eventLocation + '\'' +
                ", description='" + description + '\'' +
                ", dtStart=" + dtStart +
                ", dtEnd=" + dtEnd +
                ", eventTimeZone='" + eventTimeZone + '\'' +
                ", rRule='" + rRule + '\'' +
                ", rDate='" + rDate + '\'' +
                ", availability=" + availability +
                ", organizer='" + organizer + '\'' +
                '}';
    }
}
