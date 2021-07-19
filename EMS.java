package cognixia.jump.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EMS {

    public static void main(String[] args) {

        List<Employee> employees = new ArrayList<Employee>();

        String path = "cognixia/jump/project/";

        File file = new File(path + "/employees.csv");
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try {

            fileReader = new FileReader(file);

            bufferedReader = new BufferedReader(fileReader);

            if (file.length() == 0) {
                System.out.println("inside if");

                file = new File(path + "/employees.txt");
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
            }

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                String[] split = line.split(",");
                int id = Integer.parseInt(split[0]);
                String name = split[1];
                int salary = Integer.parseInt(split[2]);
                String department = split[3];

                employees.add(new Employee(name, salary, department));
            }


            int takingRequests = 0;

            Scanner sc = new Scanner(System.in);

            while (takingRequests != 6) {
                takingRequests = userInterface(sc, employees, path + "employees.txt", path + "employees.csv");
            }
            
            try {
                sc.close();
            } catch (Exception x) {
                System.out.println("** ERR:Failed to close the scanner **");
                x.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                bufferedReader.close();
                System.out.println("** SUCCESSFULLY CLOSED FILES **");
            } catch (IOException e) {
                System.out.println("** ERR:Failed to close file reader stream **");
                e.printStackTrace();
            }
        }

    }

    public static int userInterface(Scanner sc, List<Employee> e, String path, String path2) {
        System.out.println("==========================================================================================");
        System.out.println("Here are the available actions: ");
        System.out.println("1. Retrieve All Employees Names");
        System.out.println("2. Retrieve Single Employee");
        System.out.println("3. Retrieve All Departments");
        System.out.println("4. Remove Employee");
        System.out.println("5. Create New Employee");
        System.out.println("6. Finished");
        System.out.println("Please enter a number from 1-6 to perform an action: ");

        int chose = 0;
        
        try {
            int userAnswer = sc.nextInt();

            chose = userAnswer;

            switch (userAnswer) {
                case 1:
                    System.out.println(retrieveAllEmployeeNames(e));;
                    break;
                case 2:
                    System.out.println("Enter employee name");
                    sc.nextLine();
                    String eName = sc.nextLine();
                    System.out.println(retrieveEmployee(e, eName));
                    break;
                case 3:
                    System.out.println(getAllDepartments(e));
                    break;
                case 4:
                    System.out.println("Enter employee name");
                    sc.nextLine();
                    eName = sc.nextLine();
                    System.out.println(removeEmpolyee(e, eName));
                    break;
                case 5:
                    System.out.println("Enter employee name");
                    sc.nextLine();
                    eName = sc.nextLine();
                    System.out.println("Enter employee salary");
                    int sal = sc.nextInt();
                    System.out.println("Enter employee department");
                    sc.nextLine();
                    String department = sc.nextLine();
                    createNewEmployee(e, eName, sal, department);
                    break;
                default:
                    System.out.println("Have a nice day");
                    createFile(e, path, path2);
                    break;
            }

        } catch (Exception x) {
            System.out.println("Not a valid input");
            System.out.println("Please try again");
        }

        System.out.println("==========================================================================================");

        return chose;

    }
    

    public static String retrieveAllEmployeeNames(List<Employee> e) {
        return e.stream().map(Employee::getName).distinct().reduce((name1, name2) -> name1 + ", " + name2).get();

    }
    
    public static String retrieveEmployee(List<Employee> e, String name) {
        return e.stream().filter( x -> x.getName().equalsIgnoreCase(name)).findFirst().get().toString();
    }
    
    public static String getAllDepartments(List<Employee> e) {
        return e.stream().map(Employee::getDepartment).distinct().reduce((dept1, dept2) -> dept1 + ", " + dept2 ).get();
    }

    public static String removeEmpolyee(List<Employee> e, String name) {
        boolean wasRemoved = e.removeIf(x -> x.getName().equalsIgnoreCase(name));
        if (wasRemoved)
            return name + " was removed";
        else
            return name + " was not found";
    }

    public static String updateEmployee(List<Employee> e, String name, int salary, String department) {

        for (int i = 0; i < e.size(); i++) {
            Employee x = e.get(i);
            if (x.getName().equalsIgnoreCase(name)) {
                x.setSalary(salary);
                x.setDepartment(department);

                return x.toString();
            }
        }

        return "Not found";
    }

    public static boolean createNewEmployee(List<Employee> e, String name, int salary, String department) {
        return e.add(new Employee(name, salary, department));

    }
    
    public static void writeObjectsToFile(List<Employee> e, File file) {
        try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(file))) {

            for (int i = 0; i < e.size(); i++) {
                writer.writeObject(e.get(i));
            }

        } catch (IOException x) {
            x.printStackTrace();
        }
    }
    
    public static void createFile(List<Employee> e, String path, String path2) {
        File file = new File(path);
        File file2 = new File(path2);

        try {
            file.createNewFile();
            writeToCSV(e, file, file2);
        } catch (Exception x) {
            x.printStackTrace();
        }

    }
    

    private static final String CSV_SEPARATOR = ",";
    private static void writeToCSV(List<Employee> x , File file, File file2)
    {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            for (Employee e : x)
            {
                StringBuffer oneLine = new StringBuffer();
                oneLine.append(e.getId() <=0 ? "" : e.getId());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(e.getName().trim().length() == 0? "" : e.getName());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(e.getSalary());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(e.getDepartment());
                oneLine.append(CSV_SEPARATOR);
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }
        catch (UnsupportedEncodingException y) { y.printStackTrace();}
        catch (FileNotFoundException y){y.printStackTrace();}
        catch (IOException y) {
            y.printStackTrace();
        }
        
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2), "UTF-8"));
            for (Employee e : x)
            {
                StringBuffer oneLine = new StringBuffer();
                oneLine.append(e.getId() <=0 ? "" : e.getId());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(e.getName().trim().length() == 0? "" : e.getName());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(e.getSalary());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(e.getDepartment());
                oneLine.append(CSV_SEPARATOR);
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }
        catch (UnsupportedEncodingException y) { y.printStackTrace();}
        catch (FileNotFoundException y){y.printStackTrace();}
        catch (IOException y){y.printStackTrace();}
    }

    
}
