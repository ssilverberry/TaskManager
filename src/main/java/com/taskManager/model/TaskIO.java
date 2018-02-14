package com.taskManager.model;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskIO {
    private static final int MINUTE = 60;
    private static final int HOUR = 3600;
    private static final int DAY = 86400;
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(FORMAT);

    synchronized public static void write(TaskList tasks, OutputStream out) throws IOException, IOExcep {
        // Выходной поток, включающий методы для записи стандартных типов данных Java
        try (DataOutputStream dataOutputStream = new DataOutputStream(out);) {
            dataOutputStream.writeInt(tasks.size());
            for (Task task : tasks) {
                dataOutputStream.writeUTF(task.getTitle());
                dataOutputStream.writeBoolean(task.isActive());
                dataOutputStream.writeInt(task.getRepeatInterval());
                dataOutputStream.writeLong(task.getStartTime().getTime());
                if (task.isRepeated()) {
                    dataOutputStream.writeLong(task.getEndTime().getTime());
                }
            }
        } catch (IOException exp) {
            throw new IOExcep(exp.getMessage());
        }
    }

    synchronized public static void read(TaskList tasks, InputStream in) throws Exception {
        // Класс DataInputStream действует противоположным образом - он считывает из потока данные примитивных типов.
        try(DataInputStream readStream = new DataInputStream(in)) {
            int size = readStream.readInt();
            for (int i = 0; i < size; i++) {
                String title = readStream.readUTF();
                boolean active = readStream.readBoolean();
                long start = readStream.readLong();
                int interval = readStream.readInt();
                if (interval > 0) {
                    long end = readStream.readLong();
                    Task task = new Task(title, new Date(start), new Date(end), interval);
                    task.setActive(active);
                    tasks.add(task);
                } else {
                    Task task = new Task(title, new Date(start));
                    task.setActive(active);
                    tasks.add(task);
                }
            }
        } catch (IOException exp) {
            throw new IOExcep(exp.getMessage());
        }
    }

    public static void writeBinary(TaskList tasks, File file) throws IOException, IOExcep {
        try (OutputStream outputStream = new FileOutputStream(file);){
            write(tasks, outputStream);
        } catch (FileNotFoundException exp) {
            throw new IOExcep(exp.getMessage());
        }
    }

    public static void readBinary(TaskList tasks, File file) throws Exception, IOExcep {

        try (InputStream inputStream = new FileInputStream(file);){
            read(tasks, inputStream);
        } catch (FileNotFoundException exp) {
            throw new IOExcep(exp.getMessage());
        }

    }

    public synchronized static void writeText(TaskList tasks, File file) throws IOException, IOExcep {
        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            write(tasks, printWriter);
            printWriter.close();
        } catch (IOException exp) {
            throw new IOExcep(exp.getMessage());
        }
    }

    public static void readText(TaskList tasks, File file) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        try {
            read(tasks, bufferedReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bufferedReader.close();
    }

    public synchronized static void write(TaskList tasks, Writer out) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(out));) {
            int count = 1;

            for (Task task : tasks) {
                String title = task.getTitle();
                printWriter.print("\"" + title + "\"");

                if (task.isRepeated()) {
                    printWriter.print(" from [");
                    printWriter.print(DATE_FORMAT.format(task.getStartTime()));
                    printWriter.print("] to [");
                    printWriter.print(DATE_FORMAT.format(task.getEndTime()));
                    printWriter.print("] every [");

                    time(task.getRepeatInterval(), printWriter);
                } else {
                    printWriter.print(" at [");
                    printWriter.print(DATE_FORMAT.format(task.getTime()));
                }

                if (task.isActive()) {
                    printWriter.print("]");
                } else {
                    printWriter.print("] inactive");
                }
                if (count == tasks.size()) {
                    printWriter.print(".\n");
                } else {
                    printWriter.print(";\n");
                }
                count++;
            }
            printWriter.println("");
            printWriter.flush();
        }
    }

    public static void read(TaskList tasks, Reader in) throws Exception {

        try (BufferedReader bufferedReader = new BufferedReader(in);) {

            String line;

            while (((line = bufferedReader.readLine()) != null) && (bufferedReader.ready())) {
                int startIndex = line.indexOf("\"");
                int endIndex = line.lastIndexOf("\"");

                String title = line.substring(startIndex + 1, endIndex);

                boolean active = line.indexOf("inactive", endIndex) <= 0;

                if (line.indexOf("at [", endIndex) > 0) {
                    Date time = date(line, "at [", endIndex + 1);

                    Task task = new Task(title, new Date(time.getTime()));
                    task.setActive(active);
                    tasks.add(task);
                } else {
                    Date startTime = date(line, "from [", endIndex + 1);
                    Date endTime = date(line, "to [", endIndex + 1);

                    int startInterval = line.indexOf("every", endIndex);
                    int endInterval = line.indexOf("]", startInterval);
                    String lineInterval = line.substring(startInterval, endInterval);

                    int repeatInterval = interval("day", lineInterval) * 86400;
                    repeatInterval += interval("hour", lineInterval) * 3600;
                    repeatInterval += interval("minute", lineInterval) * 60;
                    repeatInterval += interval("second", lineInterval);

                    Task task = new Task(title, new Date(startTime.getTime()), new Date(endTime.getTime()), repeatInterval);

                    task.setActive(active);

                    tasks.add(task);
                }
            }
        }
    }

    private static void time(int seconds, PrintWriter printWriter) {
        int day = seconds / DAY;
        int hour = seconds % DAY / HOUR;
        int minute = seconds % HOUR / MINUTE;
        int second = seconds % MINUTE;

        if (day != 0) {
            if (day > 1) {
                printWriter.print(day + " days");
            } else
                printWriter.print(day + " day");
        }
        if (hour != 0) {
            if (day > 0) printWriter.print(" ");
            if (hour > 1) {
                printWriter.print(hour + " hours");
            } else {
                printWriter.print(hour + " hour");
            }
        }
        if (minute != 0) {
            if ((day > 0) || (hour > 0)) printWriter.print(" ");
            if (minute > 1) {
                printWriter.print(minute + " minutes");
            } else {
                printWriter.print(minute + " minute");
            }
        }
        if (second != 0) {
            if ((day > 0) || (hour > 0) || (minute > 0)) printWriter.print(" ");
            if (second > 1) {
                printWriter.print(second + " seconds");
            } else {
                printWriter.print(second + " second");
            }
        }
    }


    private static Date date(String line, String s, int index) throws ParseException {
        int startIndex = line.indexOf(s, index);
        int endIndex = line.indexOf("]", startIndex);

        String sDate = line.substring(endIndex - FORMAT.length(), endIndex);

        Date date = DATE_FORMAT.parse(sDate);

        return date;
    }

    private static int interval(String moment, String line) {
        if (line.indexOf(moment) <= 0)
            return 0;

        int startIndex = line.indexOf(moment) - 2;

        String result = "";
        for (int i = startIndex; i >= 0; i--) {
            char ch = line.charAt(i);
            if (!Character.isDigit(ch)) break;
            String temp = Character.toString(ch);
            result = temp.concat(result);
        }
        //System.out.println(result);
        return Integer.parseInt(result);
    }
}
