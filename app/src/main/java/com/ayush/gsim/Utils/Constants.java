package com.ayush.gsim.Utils;

import com.ayush.gsim.R;

/**
 * Created by ayush on 23/1/17.
 */

public class Constants {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0";

    public static final String STRING_LAST_FOCUS = "__LASTFOCUS";
    public static final String STRING_EVENT_TARGET = "__EVENTTARGET";
    public static final String STRING_EVENT_ARGUMENT = "__EVENTARGUMENT";
    public static final String STRING_VS_KEY = "__VSKEY";
    public static final String STRING_VIEW_STATE = "__VIEWSTATE";
    public static final String STRING_EVENT_VALIDATION = "__EVENTVALIDATION";
    public static final String STRING_VIEW_STATE_ENCRYPTED = "__VIEWSTATEENCRYPTED";
    public static final String INPUT_PATTERN_NAME = "input[name=";
    public static final String INPUT_CAPTCHA_ID = "img[id=Image1]";
    public static final String INPUT_TYPE_TEXT = "input[type=text]";

    public static final String INPUT_LAST_FOCUS = INPUT_PATTERN_NAME + STRING_LAST_FOCUS + "]";
    public static final String INPUT_EVENT_TARGET = INPUT_PATTERN_NAME + STRING_EVENT_TARGET + "]";
    public static final String INPUT_EVENT_ARGUMENT = INPUT_PATTERN_NAME + STRING_EVENT_ARGUMENT + "]";
    public static final String INPUT_VS_KEY = INPUT_PATTERN_NAME + STRING_VS_KEY + "]";
    public static final String INPUT_VIEW_STATE = INPUT_PATTERN_NAME + STRING_VIEW_STATE + "]";
    public static final String INPUT_EVENT_VALIDATION = INPUT_PATTERN_NAME + STRING_EVENT_VALIDATION + "]";

    public static final String INPUT_VIEW_STATE_ENCRYPTED = INPUT_PATTERN_NAME + STRING_VIEW_STATE_ENCRYPTED + "]";
    public static final String LOGIN_BUTTON_ID = "btnLogin__";
    public static final String LOGIN_TEXT_PASSWORD = "txtPass";
    public static final String LOGIN_TEXT_USERNAME = "txtUserName";
    public static final String LOGIN_TEXT_BIRTH = "txtdateofBirth";
    public static final String LOGIN_HID_SET_NAME = "hidsetname";


    public static final String FONT_ROBOTO_THIN = "robotothin.ttf";
    public static final String FONT_ROBOTO_MEDIUM = "robotomedium.ttf";

    public static final String URL_LOGIN = "http://122.160.168.157/isimgc/login";
    public static final String URL_HOME = "http://122.160.168.157/isimgc/home";
    public static final String URL_COURSE = "http://122.160.168.157/iSIMGC/Student/Course";
    public static final String URL_TIMETABLE = "http://122.160.168.157/iSIMGC/Student/TimeTable";
    public static final String URL_ATTENDANCE = "http://122.160.168.157/iSIMGC/Student/TodayAttendence";
    public static final String URL_LIBRARY = "http://122.160.168.157/iSIMGC/Library/LibIssueReturn";
    public static final String URL_COURSE_PLAN = "http://122.160.168.157/iSIMGC/Student/CoursePlanSubjectTopic";

    public static final String COOKIE_ASP_NET = "ASP.NET_SessionId";


    public static final String OLD_SHARED_PREF_LOGIN_TABLE = "QPQ";
    public static final String OLD_SHARED_PREF_LOGIN_TABLE_1 = "MM";
    public static final String OLD_SHARED_PREF_LOGIN_TABLE_2 = "NN";
    public static final String OLD_SHARED_PREF_ADS_COUNT_TABLE = "ads_count1";
    public static final String OLD_SHARED_PREF_DIALOG_COUNT_TABLE = "Dialog_count1";
    public static final String OLD_SHARED_PREF_ATTENDANCE_1_TABLE = "Dates";
    public static final String OLD_SHARED_PREF_ATTENDANCE_2_TABLE = "AttendanceCount";

    public static final String SHARED_PREF_LOGIN_TABLE = "LOGIN";
    public static final String SHARED_PREF_AUTO_LOGIN = "autoLogin";
    public static final String SHARED_PREF_USERNAME = "username";
    public static final String SHARED_PREF_PASSWORD = "password";
    public static final String AUTO_LOGIN_TRUE_CASE = "1";
    public static final String AUTO_LOGIN_FALSE_CASE = "0";


    public static final String SHARED_PREF_ADS_COUNT_TABLE = "adsCount";
    public static final String SHARED_PREF_HOME_ADS_COUNT = "home";
    public static final String SHARED_PREF_LOGIN_ADS_COUNT = "login";
    public static final String SHARED_PREF_NEWS_ADS_COUNT = "news";
    public static final String SHARED_PREF_ARCHIVE_ADS_COUNT = "archive";
    public static final String SHARED_PREF_CHAT_ADS_COUNT = "chat";
    public static final String SHARED_PREF_VERSION_ALERT_ADS_COUNT = "versionAlert";

    public static final String SHARED_PREF_ATTENDANCE_TABLE = "attendance";
    public static final String SHARED_PREF_ATTENDANCE_MONDAY = "Monday";
    public static final String SHARED_PREF_ATTENDANCE_SATURADAY = "Saturday";


    public static final String USERNAME_INTENT = "username";
    public static final String PASSWORD_INTENT = "password";
    public static final String COOKIE_INTENT = "cookie";
    public static final String BRANCH_INTENT = "branch";
    public static final String STUDENT_NAME_INTENT = "name";

    public static final String TEMP_MENU_INTENT = "Menu";
    public static final String TEMP_USERNAME_INTENT = "User";
    public static final String TEMP_PASSWORD_INTENT = "Pass";
    public static final String TEMP_COOKIE_INTENT = "Cook";

    public static final String COURSE_PLAN_SUBJECTS_INTENT = "subjects";
    public static final String COURSE_PLAN_SUBJECT_IDS_INTENT = "subject_ids";

    public static final Integer[] attendanceImages = {

            R.drawable.attendance_total
            , R.drawable.lecture
            , R.drawable.today_attendance
            , R.drawable.datewise_calendar

    };

    public static final String[] attendanceValues = {"Attendance", "Subject Wise", "Today", "Date Wise"};
    public static final Integer[] erpImages = {
            R.drawable.timetable,

            R.drawable.course_plan,
            R.drawable.library,
    };
    public static final String[] erpValues = {"Time Table", "Course Plan", "Library"};

    public static final Integer[] optionImages = {
            R.drawable.newspaper
            , R.drawable.chat
            , R.drawable.archive
    };
    public static final String[] optionsValues = {"News Feed", "Chat", " Archive Pad"};
    public static final String ADVERTISE_URL="https://iamayushjain.github.io/advertiseGsim.html";
    public static final String HOME_PAGE_LIST_TYPE_ATTENDANCE = "attendance";
    public static final String HOME_PAGE_LIST_TYPE_ERP = "erp";
    public static final String HOME_PAGE_LIST_TYPE_OPTIONS = "options";

    public static final String ATTENDANCE_DATABASE = "DemoDatabase1";
    public static final String ATTENDANCE_DATABASE_TABLE_NAME = "u";
    public static final String ATTENDANCE_SEMESTER_STARTING_MONDAY = "16/01/2017";
    public static final String ATTENDANCE_SEMESTER_STARTING_SATURDAY = "21/01/2017";

    public static final String BRANCH_WISE_CHAT_ACTIVE_TRUE = "active";
    public static final String APP_URL = "https://play.google.com/store/apps/details?id=com.ayush.gsim";
}
