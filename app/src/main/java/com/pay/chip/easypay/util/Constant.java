package com.pay.chip.easypay.util;

/**
 * Created by Administrator on 2016/3/16.
 */
public class Constant {

    public static String KEY_TITLE = "KEY_TITLE";
    public static String KEY_TIME = "KEY_TIME";
    public static String KEY_CONTENT_ID = "KEY_CONTENT_ID";

    public static final int CODE_SUCCESS = 1;
    public static int CODE_FAIL = -1;


    public static String HOST = "http://chip.applinzi.com/index.php/Home/";

    private static String USER_LOGIN = "User/login";
    private static String USER_REGISTER = "User/register";

    private static String STUDENT_FIND = "Student/find";
    private static String TEACHER_FIND = "Teacher/find";
    private static String WORK_CONTENT_UNFINISHED = "Work_content/findUnfinished";
    private static String WORK_CONTENT_FINISHED = "Work_content/findFinished";
    private static String WORK_CONTENT_ALL = "WorkContent/findAll";
    private static String WORK_CONTENT_ADD = "WorkContent/add";
    private static String TEACHER_LOGIN = "Teacher/login";
    private static String WORK_ANSWER = "Work_answer/findAnswer";
    private static String WORK_SUBMIT = "Work_progress/submitAnswer";

    private static String TEACHER_REGISTER = "Teacher/register";
    public static final String USER = "user";
    public static final String ID = "userId";
    public static final String S_ID = "s_id";
    public static final String CONTENT_ID = "w_c_id";
    public static final String TOPIC = "topic";
    public static final String CONTENT = "content";
    public static final String CONTENT_ANSWER = "answer_content";
    public static final String PASS = "pass";
    public static final String TELNO = "telno";

    public static String HOST_USER_LOGIN = HOST + USER_LOGIN;
    public static String HOST_USER_REGISTER = HOST + USER_REGISTER;
    public static String HOST_STUDENT_FIND = HOST + STUDENT_FIND;
    public static String HOST_TEACHER_FIND = HOST + TEACHER_FIND;
    public static String HOST_STUDENT_WORK_UNFINISHED = HOST + WORK_CONTENT_UNFINISHED;
    public static String HOST_STUDENT_WORK_FINISHED = HOST + WORK_CONTENT_FINISHED;
    public static String HOST_STUDENT_WORK_ALL = HOST + WORK_CONTENT_ALL;
    public static String HOST_ADD_WORK = HOST + WORK_CONTENT_ADD;
    public static String HOST_TEACHER_LOGIN = HOST + TEACHER_LOGIN;

    public static String HOST_WORK_ANSWER = HOST + WORK_ANSWER;
    public static String HOST_WORK_SUBMIT = HOST + WORK_SUBMIT;
    public static String HOST_TEACHER_REGISTER = HOST + TEACHER_REGISTER;
}
