package indi.github.icear.simpleclass.module.calendardata;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import indi.github.icear.simpleclass.module.calendardata.entity.CalendarInfo;
import indi.github.icear.simpleclass.module.calendardata.entity.EventInfo;

/**
 * Created by icear on 2017/11/6.
 * CalendarDataUProvider
 * 提供Android端Calendar数据操作API
 */

public class CalendarDataProvider {
    private static final String TAG = CalendarDataProvider.class.getSimpleName();

    private ContentResolver cr;

    public CalendarDataProvider(ContentResolver cr) {
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


        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        try (Cursor cur = cr.query(uri, EVENT_PROJECTION, null, null, null)) {
            if (cur != null) {
                CalendarInfo calendarInfo;
                while (cur.moveToNext()) {
                    calendarInfo = new CalendarInfo();
                    calendarInfo.setCalendarId(cur.getLong(PROJECTION_ID_INDEX));
                    calendarInfo.setCalendarDisplayName(cur.getString(PROJECTION_DISPLAY_NAME_INDEX));
                    calendarInfo.setAccountName(cur.getString(PROJECTION_ACCOUNT_NAME_INDEX));
                    calendarInfoList.add(calendarInfo);
                }
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
     * @return 如果存在返回日历id，否则返回-1
     * @throws SecurityException 没有读取日历权限时触发异常
     */
    public long checkCalendarAccountExist(String accountName,
                                          String accountType,
                                          String ownerAccount) throws SecurityException {
        final String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,                           // 0
        };
        final int PROJECTION_ID_INDEX = 0;

        long calendarId = -1;

        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[]{accountName, accountType, ownerAccount};

        try (Cursor cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null)) {
            if (cur != null) {
                if (cur.moveToNext()) {
                    calendarId = cur.getLong(PROJECTION_ID_INDEX);
                }
            }
        }
        return calendarId;
    }

    /**
     * 根据传入的日历信息尝试删除某个日历
     * 需求：calendarId，accountName
     *
     * @param calendarInfo 日历信息
     * @return 删除的数量
     */
    public int deleteCalendarById(CalendarInfo calendarInfo) {
        //以同步适配器身份操作日历
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        Uri uriWithRight = uri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, calendarInfo.getAccountName())
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
        Uri pointedUriWithRight = ContentUris.withAppendedId(uriWithRight, calendarInfo.getCalendarId());
        return cr.delete(pointedUriWithRight, null, null);
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
//        calendar.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, );

        //以同步适配器身份插入日历
        Uri calendarUri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, calendarInfo.getAccountName())
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();

        Uri result = cr.insert(calendarUri, calendar);

        return result == null ? -1 : ContentUris.parseId(result);
    }

    /**
     * 根据传入的EventInfo创建新的Event
     * 需求：calendarId，dtStart，eventTimeZone
     *
     * @param event EventInfo
     * @return 新Event的ID
     * @throws SecurityException 没有写入日历的权限时抛出异常
     */
    public long createNewEvent(EventInfo event) throws SecurityException {
        ContentValues values = new ContentValues();
        //必填项
        values.put(CalendarContract.Events.CALENDAR_ID, event.getCalendarId());//事件所属的日历id
        values.put(CalendarContract.Events.DTSTART, event.getDtStart());//事件的开始时间
        values.put(CalendarContract.Events.EVENT_TIMEZONE, event.getEventTimeZone());//事件的时区

        //非必填项，检查未设置的则跳过
        if (event.getOrganizer() != null) {
            values.put(CalendarContract.Events.ORGANIZER, event.getOrganizer());//事件的组织者
        }

        if (event.getTitle() != null) {
            values.put(CalendarContract.Events.TITLE, event.getTitle());//事件的标题
        }

        if (event.getEventLocation() != null) {
            values.put(CalendarContract.Events.EVENT_LOCATION, event.getEventLocation());//事件的发生地点
        }

        if (event.getDescription() != null) {
            values.put(CalendarContract.Events.DESCRIPTION, event.getDescription());//事件的描述
        }

        if (event.getDtEnd() != 0L) {
            values.put(CalendarContract.Events.DTEND, event.getDtEnd());//事件的结束时间
        }

        if (event.getrRule() != null) {
            values.put(CalendarContract.Events.RRULE, event.getrRule());//事件的重复发生规则
        }

        if (event.getrDate() != null) {
            values.put(CalendarContract.Events.RDATE, event.getrDate());//和上一个组合使用
        }

        values.put(CalendarContract.Events.AVAILABILITY, event.getAvailability());//是否为忙碌事件，默认值为0表示不是忙碌事件


        Uri result = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        return result == null ? -1 : ContentUris.parseId(result);
    }

}
