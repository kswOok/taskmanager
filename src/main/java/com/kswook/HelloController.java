package com.kswook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kswook.bean.ReturnData;
import com.kswook.bean.TaskBean;
import com.kswook.bean.TaskResultBean;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class HelloController {

    static JSONObject task = null;

    public static final String taskLYL = "./task/lyl.txt";
    public static final String taskDG = "./task/dg.txt";

    public static List<TaskResultBean> lastResult = new ArrayList<>();

    public static final String TaskResult = "./task/result.txt";

    public static final OkHttpClient client = new OkHttpClient();

    @RequestMapping(path = "/task/query", method = RequestMethod.GET)
    public ReturnData index(@RequestParam String type) {
        String lylTaskContent = readFile(taskLYL);
        String dgTaskContent = readFile(taskDG);
        List<TaskBean> taskBeans = new ArrayList<>();
        if (!StringUtils.isEmpty(lylTaskContent)) {
            taskBeans.addAll(JSONArray.parseArray(lylTaskContent, TaskBean.class));
        }
        if ("ALL".equals(type)) {
            if (!StringUtils.isEmpty(dgTaskContent)) {
                taskBeans.addAll(JSONArray.parseArray(dgTaskContent, TaskBean.class));
            }
        }
        return ReturnData.success().data(taskBeans);

    }

    @RequestMapping(path = "/task/modify", method = RequestMethod.POST)
    public String modify(@RequestBody List<TaskBean> taskBeans, @RequestParam String type) {
        String taskContent = JSONArray.toJSONString(taskBeans);
        if ("LYL".equals(type)) {
            writeFile(taskLYL, taskContent);
        } else if ("ALL".equals(type)) {
            writeFile(taskDG, taskContent);
        }
        return taskContent;
    }

    @RequestMapping(path = "/task/commit", method = RequestMethod.POST)
    public String commit(@RequestBody List<TaskResultBean> taskBeans) {
        if (lastResult.isEmpty()) {
            String localLastResult = readFile(TaskResult);
            if (!StringUtils.isEmpty(localLastResult)) {
                lastResult = JSONArray.parseArray(localLastResult, TaskResultBean.class);
            }
        }
        for (int i = 0; i < taskBeans.size(); i++) {
            boolean containe = false;
            for (TaskResultBean taskResultBean : lastResult) {
                if (taskBeans.get(i).tag.equals(taskResultBean.tag)) {
                    taskResultBean.count += taskBeans.get(i).count;
                    taskResultBean.doubleCount += taskBeans.get(i).doubleCount;
                    taskResultBean.thirdCount += taskBeans.get(i).thirdCount;
                    taskResultBean.failedCount += taskBeans.get(i).failedCount;
                    taskResultBean.missCount += taskBeans.get(i).missCount;
                    containe = true;
                }
            }
            if (!containe) {
                lastResult.add(taskBeans.get(i));
            }
        }
        writeFile(TaskResult, JSONArray.toJSONString(lastResult));
        return JSONArray.toJSONString(lastResult);
    }

    @RequestMapping(path = "/task/clearResult", method = RequestMethod.GET)
    public String clearResult() {
        lastResult.clear();
        writeFile(TaskResult, "");
        return JSONArray.toJSONString(lastResult);
    }


    @RequestMapping(path = "/task/queryResult", method = RequestMethod.GET)
    public ReturnData queryResult() {
        if (lastResult.isEmpty()) {
            String localLastResult = readFile(TaskResult);
            if (!StringUtils.isEmpty(localLastResult)) {
                lastResult = JSONArray.parseArray(localLastResult, TaskResultBean.class);
            }
        }
        return ReturnData.success().data(lastResult);
    }


    @RequestMapping(path = "/task/current", method = RequestMethod.GET)
    public List<TaskBean> current(@RequestParam(value = "type", required = false) String type
    ) {
        Request request = null;
        if ("ws".equals(type)) {
            request = new Request.Builder().url("http://129.204.28.211/tasksws.json").build();
        } else if ("all".equals(type)) {
            request = new Request.Builder().url("http://129.204.28.211/tasks.json").build();
        } else {
            request = new Request.Builder().url("http://129.204.28.211/taskslyl.json").build();

        }

//        Request request = new Request.Builder().url("http://127.0.0.1/tasks.json").build();
//        Request request = new Request.Builder().url("http://129.204.28.211/taskslyl.json").build();
        String result = "";
        try {
            result = client.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(request)) {
            List<TaskBean> taskBeans = JSONArray.parseArray(result, TaskBean.class);
            List<TaskBean> resultTask = new ArrayList<>();
            for (TaskBean taskBean : taskBeans) {
                try {
                    if (taskBean.isEffect()) {
                        if (taskBean.url != null && !taskBean.url.contains("TaskSelfFire")) {
                            resultTask.add(taskBean);
                        } else if (taskBean.urls != null && !taskBean.urls.isEmpty()) {
                            resultTask.add(taskBean);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!resultTask.isEmpty()) {
                return resultTask;
            }
        }
        return null;
    }


    @RequestMapping(path = "/task/next", method = RequestMethod.GET)
    public List<TaskBean> next(@RequestParam(value = "type", required = false) String type) {
//        Request request = new Request.Builder().url("http://127.0.0.1/tasks.json").build();

        Request request = null;
        if ("ws".equals(type)) {
            request = new Request.Builder().url("http://129.204.28.211/tasksws.json").build();
        } else if ("all".equals(type)) {
            request = new Request.Builder().url("http://129.204.28.211/tasks.json").build();
        } else {
            request = new Request.Builder().url("http://129.204.28.211/taskslyl.json").build();

        }
        String result = "";
        try {
            result = client.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(request)) {
            List<TaskBean> taskBeans = JSONArray.parseArray(result, TaskBean.class);
            List<TaskBean> resultTask = new ArrayList<>();
            for (TaskBean taskBean : taskBeans) {
                try {
                    if (taskBean.isNextDayEffect()) {
                        if (taskBean.url != null && !taskBean.url.contains("TaskSelfFire")) {
                            resultTask.add(taskBean);
                        } else if (taskBean.urls != null && !taskBean.urls.isEmpty()) {
                            resultTask.add(taskBean);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!resultTask.isEmpty()) {
                return resultTask;
            }
        }
        return null;
    }

    public static String readFile(String path) {
        String content = "";
        try {
            File file = new File(path);
            if (!file.exists()) {
                return "";
            }
            Source source = Okio.source(file);
            BufferedSource bufferedSource = Okio.buffer(source);
            content = bufferedSource.readUtf8();
            bufferedSource.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void writeFile(String path, String content) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            Sink sink = Okio.sink(file);
            BufferedSink bufferedSink = Okio.buffer(sink);
            bufferedSink.writeUtf8(content);
            bufferedSink.flush();
            bufferedSink.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}