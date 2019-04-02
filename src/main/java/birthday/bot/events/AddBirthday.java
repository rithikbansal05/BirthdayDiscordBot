package birthday.bot.events;

import birthday.bot.main.Info;
import javafx.beans.property.SimpleListProperty;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.swing.text.DateFormatter;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AddBirthday extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split(" ");

        if(args[0].equalsIgnoreCase( Info.prefix + "add")){
            if(!event.getMember().getUser().isBot()){
                String birthDate = returnDateLocForm(args[1]);

                String user = event.getMember().getUser().getName();
                boolean isAlreadyThere = checkFile(user);

                if(birthDate != "" && !isAlreadyThere){
                    event.getChannel().sendMessage(
                        event.getMember().getUser().getName() +
                        "'s birthday (i.e. on " + birthDate + ") has been added." ).queue();
                    try{
                        Date d1 = new SimpleDateFormat("MM-dd-yyyy").parse(birthDate);
                        addDataToMapMain(user,d1);
                    } catch(Exception e){
                        System.out.println("Error occured 4");
                    }

                } else if(isAlreadyThere){
                    event.getChannel().sendMessage("Your birthday is already on the list.").queue();
                }
                else
                    event.getChannel().sendMessage("Try again. Use date format MM-dd-yyyy").queue();
            }
        }
    }

    public static boolean checkMap(String user){
        return Info.birthDateRecorder.containsKey(user);
    }

    public static boolean checkFile(String user){
        boolean val = false;
        try{

            File file = new File("birthday.txt");
            boolean exists = file.exists();
            if(exists) {
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String[] data = sc.nextLine().split(",");
                    //System.out.println(data[0]);
                    String key = data[0];
                    key = key.substring(2);
                    System.out.println(key);
                    System.out.println(user);
                    if (key.equalsIgnoreCase(user))
                        val = true;
                }
            }
        }catch(Exception e){
            System.out.println("Error occured 5.");
        }

        return val;
    }

    public static void addDataToMapMain(String name, Date birth){
        //read the data from the text file when the bot comes online.
        //gets the rest of data from the txt file if any
        if(Info.count == 0){
            File tmp = new File("birthday.txt");
            boolean exists = tmp.exists();
            if(exists)
                addDataToMap();
            Info.count++;
        }

        //add the new data to the map
        if(!checkMap(name)){
            //System.out.println(Info.birthDateRecorder.get("RithikBansal"));
            Info.birthDateRecorder.put(name, birth);

            //append the unique new data to the txt file
            try{
                FileOutputStream outputStrm = new FileOutputStream("birthday.txt", true);
                DataOutputStream outStrm = new DataOutputStream(new BufferedOutputStream(outputStrm));
                String value  = name + "," + birth + "\n";
                outStrm.writeUTF(value);
                outStrm.close();
            } catch (Exception e){
                System.out.println("Error occured 3");
            }
        }
    }

    public static void addDataToMap(){
        try{
            File file = new File("birthday.txt");
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                String[] data = sc.nextLine().split(",");
                String key = data[0].substring(1);
                String value = data[1];
                String[] date = value.split(" ");
                String dt = date[0]+", "+date[1]+" "+date[2] + " " + date[5];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd yyyy");
                LocalDate dateTime = LocalDate.parse(dt,formatter);
                Date finalDate = java.sql.Date.valueOf(dateTime);

                Info.birthDateRecorder.put(key,finalDate);
            }
        }catch(Exception e){
            System.out.println("Error occured 1.");
        }
    }

    /*public static void writeToTXTFile(HashMap<String,Date> map){
        try{
            FileOutputStream outputStrm = new FileOutputStream("birthday.txt");
            DataOutputStream outStrm = new DataOutputStream(new BufferedOutputStream(outputStrm));
            for(String val: map.keySet()) {
                String value = val + "," + map.get(val) + "\n";
                outStrm.writeUTF(value);
            }
            outStrm.close();
        } catch (Exception e){
            System.out.println("Error occured 2");
        }
    }*/

    public static boolean isDateVal(String date){
        SimpleDateFormat dateForm = new SimpleDateFormat("MM-dd-yyyy");
        dateForm.setLenient(false);

        try {
            dateForm.parse(date.trim());
        } catch (ParseException pe) {
            System.out.println("Incorrect date format. Try another one.");
            //System.exit(0);
            return false;
        }
        return true;
    }

    public static String returnDateLocForm(String inDate){
        String outDat = "";

        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        Date outputDate;
        //formatter = new SimpleDateFormat("MM/dd/yyyy");

        if(isDateVal(inDate)){
            try {
                outputDate = df.parse(inDate);
                outDat = df.format(outputDate);
            } catch (ParseException pe){
                System.out.println("Incorrect date format");
                //System.exit(0);
            }
        }
        //System.out.println(outDat);
        return outDat;
    }
}
