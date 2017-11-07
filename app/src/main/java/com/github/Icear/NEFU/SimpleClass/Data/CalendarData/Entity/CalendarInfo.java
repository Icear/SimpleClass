package com.github.Icear.NEFU.SimpleClass.Data.CalendarData.Entity;

/**
 * Created by icear on 2017/11/6.
 * 记录Calendar相关信息的实体类
 */

public class CalendarInfo {
    private long calendarId;
    private String accountName;//The account that was used to sync the entry to the device
    private String accountType;//The type of the account that was used to sync the entry to the device. A type of ACCOUNT_TYPE_LOCAL will keep this event form being deleted if there are no matching accounts on the device.
    private String name;//The name of the calendar. Column name
    private String calendarDisplayName;//The display name of the calendar. Column name.
    private String ownerAccount;//calendar Owner
    private int calendarColor;//The color of the calendar. This should only be updated by the sync adapter, not other apps, as changing a calendar's color can adversely affect its display.
    private int calendarAccessLevel;//The level of access that the user has for the calendar

    private boolean visible;//set Calendar is visible to user
    private boolean syncEvent;//Is this calendar synced and are its events stored on the device? 0 - Do not sync this calendar or store events for this calendar. 1 - Sync down events for this calendar.
    private String calendarTimeZone;//The time zone the calendar is associated with.

    public long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(long calendarId) {
        this.calendarId = calendarId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCalendarDisplayName() {
        return calendarDisplayName;
    }

    public void setCalendarDisplayName(String calendarDisplayName) {
        this.calendarDisplayName = calendarDisplayName;
    }

    public int getCalendarColor() {
        return calendarColor;
    }

    public void setCalendarColor(int calendarColor) {
        this.calendarColor = calendarColor;
    }

    public int getCalendarAccessLevel() {
        return calendarAccessLevel;
    }

    public void setCalendarAccessLevel(int calendarAccessLevel) {
        this.calendarAccessLevel = calendarAccessLevel;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getOwnerAccount() {
        return ownerAccount;
    }

    public void setOwnerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
    }

    public boolean isSyncEvent() {
        return syncEvent;
    }

    public void setSyncEvent(boolean syncEvent) {
        this.syncEvent = syncEvent;
    }

    public String getCalendarTimeZone() {
        return calendarTimeZone;
    }

    public void setCalendarTimeZone(String calendarTimeZone) {
        this.calendarTimeZone = calendarTimeZone;
    }

}
