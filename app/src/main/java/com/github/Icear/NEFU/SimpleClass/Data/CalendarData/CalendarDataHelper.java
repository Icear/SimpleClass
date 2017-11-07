package com.github.Icear.NEFU.SimpleClass.Data.CalendarData;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;

import com.github.Icear.NEFU.SimpleClass.Data.CalendarData.Entity.CalendarInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by icear on 2017/11/6.
 * CalendarDataUtil
 * 用于辅助进行Android端Calendar数据操作
 */

public class CalendarDataHelper {

    private ContentResolver cr;

    public CalendarDataHelper(ContentResolver cr) {
        this.cr = cr;
    }

    /**
     * 查询所有的Calendar
     *
     * @return CalendarList
     * @throws SecurityException 没有访问日历的权限
     */
    public List<CalendarInfo> queryCalendar() throws SecurityException {

        List<CalendarInfo> calendarInfoList = new ArrayList<>();

        final String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,                           // 0
                CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
        };

        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        final int PROJECTION_DISPLAY_NAME_INDEX = 2;
        final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

        Cursor cur = null;
        try {
            Uri uri = CalendarContract.Calendars.CONTENT_URI;

            // Submit the query and get a Cursor object back.
            cur = cr.query(uri, EVENT_PROJECTION, null, null, null);

            // Use the cursor to step through the returned records
            if (cur != null) {
                CalendarInfo calendarInfo;
                while (cur.moveToNext()) {
                    calendarInfo = new CalendarInfo();

                    // Get the field values
                    calendarInfo.setCalendarId(cur.getLong(PROJECTION_ID_INDEX));
                    calendarInfo.setCalendarDisplayName(cur.getString(PROJECTION_DISPLAY_NAME_INDEX));
                    calendarInfo.setAccountName(cur.getString(PROJECTION_ACCOUNT_NAME_INDEX));
                    calendarInfo.setOwnerAccount(cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX));

                    // Do something with the values...
                    calendarInfoList.add(calendarInfo);
                }
            }
        } finally {
            if (cur != null) {
                cur.close();
            }
        }

        return calendarInfoList;
    }

    /**
     * 查询某个日历帐户是否存在
     *
     * @param accountName  帐户名
     * @param accountType  帐户类型
     * @param ownerAccount 拥有者
     * @return 查询结果
     * @throws SecurityException 没有读取日历权限时触发异常
     */
    public boolean checkCalendarAccountExist(String accountName,
                                             String accountType,
                                             String ownerAccount) throws SecurityException {
        final String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,                           // 0
        };

        Cursor cur = null;
        try {
            Uri uri = CalendarContract.Calendars.CONTENT_URI;
            String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                    + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                    + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
            String[] selectionArgs = new String[]{accountName, accountType, ownerAccount};
            // Submit the query and get a Cursor object back.
            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

            // Use the cursor to step through the returned records
            return cur != null && cur.getCount() > 0;
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
    }

    /**
     * 创建指定日历帐户
     *
     * @param calendarInfo 日历帐户信息
     * @return 创建成功返回日历id，失败返回-1
     * @throws SecurityException 没有写入日历权限时抛出异常
     */
    public long createNewCalendarAccount(@NonNull CalendarInfo calendarInfo) throws SecurityException {
        ContentValues calendar = new ContentValues();
        calendar.put(CalendarContract.Calendars.NAME, calendarInfo.getName());
        calendar.put(CalendarContract.Calendars.ACCOUNT_NAME, calendarInfo.getAccountName());
        calendar.put(CalendarContract.Calendars.ACCOUNT_TYPE, calendarInfo.getAccountType());
        calendar.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, calendarInfo.getCalendarDisplayName());
        calendar.put(CalendarContract.Calendars.VISIBLE, calendarInfo.isVisible() ? 1 : 0);
        calendar.put(CalendarContract.Calendars.CALENDAR_COLOR, calendarInfo.getCalendarColor());
        calendar.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, calendarInfo.getCalendarAccessLevel());
        calendar.put(CalendarContract.Calendars.SYNC_EVENTS, calendarInfo.isSyncEvent() ? 1 : 0);
        calendar.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, calendarInfo.getCalendarTimeZone());
        calendar.put(CalendarContract.Calendars.OWNER_ACCOUNT, calendarInfo.getOwnerAccount());
        calendar.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, calendarInfo.getName());

        Uri calendarUri = CalendarContract.Calendars.CONTENT_URI;

        Uri result = cr.insert(calendarUri, calendar);

        return result == null ? -1 : ContentUris.parseId(result);
    }
}
