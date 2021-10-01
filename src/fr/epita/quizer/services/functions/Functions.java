/**
 * Class regrouping The different Functions needed for the Logic
 */
package fr.epita.quizer.services.functions;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import fr.epita.quizer.datamodel.MCQChoice;
import fr.epita.quizer.datamodel.Question;
import fr.epita.quizer.datamodel.Quiz;
import fr.epita.quizer.services.dao.MCQChoiceDAO;
import fr.epita.quizer.services.dao.QuestionDAO;
import fr.epita.quizer.services.dao.QuizDAO;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Functions {

    /**
     * This method Give the List of Answers Choices grouped by Question
     * @throws Exception
     */
    public static void groupChoiceByQuestion() throws Exception {
        QuestionDAO dao = new QuestionDAO();
        MCQChoiceDAO mcqChoiceDAO = new MCQChoiceDAO();
        List<Question> questionList = new ArrayList<>();
        questionList = dao.readALl();
        int increm = 1;
        for(Question question:questionList){
            System.out.println("Number:"+increm+" Question:"+question.getQuestion());
            List<MCQChoice> mcqChoices = mcqChoiceDAO.search(question.getId());
            for (MCQChoice mcqChoice:mcqChoices){
                System.out.println("\t R_ID:"+mcqChoice.getId()+" Value:"+mcqChoice.getChoice());
            }
            System.out.println("\n\n");
            increm++;
        }
        System.out.println("\n\n\n");
    }

    /**
     * Function To export Quiz into Plain text format to ./Export folder
     * @throws Exception
     */
    public static void exportTxt() throws Exception {
        QuestionDAO dao = new QuestionDAO();
        MCQChoiceDAO mcqChoiceDAO = new MCQChoiceDAO();
        List<Question> questionList = new ArrayList<>();
        questionList = dao.readALl();
        int increm = 1;
        try {
            FileWriter myWriter = new FileWriter("./Exports/Quizexport.txt");
            myWriter.write("************** Quiz "+new Date()+"************");
            for(Question question:questionList){ // For Each QUestion I print the Question followed by Answers
                myWriter.write("\n\n\nNumber:"+increm+" Question:"+question.getQuestion()+"\n");
                List<MCQChoice> mcqChoices = mcqChoiceDAO.search(question.getId());
                for (MCQChoice mcqChoice:mcqChoices){
                    myWriter.write("\t R_ID:"+mcqChoice.getId()+" Value:"+mcqChoice.getChoice()+"\n");
                }
                System.out.println("\n\n");
                increm++;
            }
            myWriter.write("\n\n\n                                                                      By Issaka SANFO");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("\n\n\n");
    }

    /**
     * Function To export Quiz into PDF format ./Export folder
     * @throws Exception
     */
    public static void exportPdf() throws Exception {
        QuestionDAO dao = new QuestionDAO();
        MCQChoiceDAO mcqChoiceDAO = new MCQChoiceDAO();
        List<Question> questionList = new ArrayList<>();
        questionList = dao.readALl();
        int increm = 1;
        Document document = new Document();
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("./Exports/Quizexport.pdf"));
            document.open();
            document.add(new Paragraph("************** Quiz "+new Date()+"************"));
            for(Question question:questionList){ // For Each QUestion I print the Question followed by Answers
                document.add(new Paragraph("\n\n\nNumber:"+increm+" Question:"+question.getQuestion()+"\n"));
                List<MCQChoice> mcqChoices = mcqChoiceDAO.search(question.getId());
                for (MCQChoice mcqChoice:mcqChoices){
                    document.add(new Paragraph("\t R_ID:"+mcqChoice.getId()+" Value:"+mcqChoice.getChoice()+"\n"));
                }
                System.out.println("\n\n");
                increm++;
            }
            document.add(new Paragraph(("\n\n\n                                                                                            By Issaka SANFO")));
            document.close();
            writer.close();
        } catch (DocumentException e)
        {
            e.printStackTrace();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        System.out.println("\n\n\n");
    }

    /**
     * Method for Admin Panel Call
     * @throws Exception
     */
    public static void adminPanel() throws Exception {
        QuestionDAO dao = new QuestionDAO();
        MCQChoiceDAO mcqChoiceDAO = new MCQChoiceDAO();
        QuizDAO quizDAO = new QuizDAO();
        Scanner ScanUserId = new Scanner(System.in);
        Integer menuID = 0;
        do { // 10 Menus, if the User enter Bad value an exception is thrown
            System.out.println("Type Number to go to the menu");
            System.out.println(" 1) Create a Question");
            System.out.println(" 2) List all Questions");
            System.out.println(" 3) Modify a Question");
            System.out.println(" 4) Delete a Question");
            System.out.println(" 5) Search a Question");
            System.out.println(" 6) Add MCQ Choices");
            System.out.println(" 7) Add Quiz");
            System.out.println(" 8) Export Quiz in TXT");
            System.out.println(" 9) Export Quiz in PDF");
            System.out.println(" 10) Student Panel\n");
            String str = ScanUserId.nextLine();

            try {
                Integer.parseInt(str);
                //System.out.println(str + " is a valid integer");
                menuID = Integer.parseInt(str);
            }
            catch (NumberFormatException e)
            {
                System.out.println(str + " is not a valid integer");
            }

        } while (menuID != 1 && menuID != 2 && menuID !=3 && menuID !=4 && menuID !=5 && menuID !=6 && menuID !=7 && menuID !=8 && menuID !=9 && menuID !=10);

        //Switch to Execute a Logic Depending On the User Choice
        switch (menuID) {
            case 1:
                Scanner numberTopics = new Scanner(System.in);
                Integer numTopics=0;
                do {
                    System.out.println("Fistly, input number of Topics for the Question between 1 and 5:");
                    String str = ScanUserId.nextLine();
                    try
                    {
                        Integer.parseInt(str);
                        //System.out.println(str + " is a valid integer");
                        numTopics = Integer.parseInt(str);
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println(str + " is not a valid integer");
                    }
                } while (numTopics != 1 && numTopics != 2 && numTopics != 3 && numTopics != 4 && numTopics != 5);

                String[] topicsArray = new String[numTopics];
                for (int i = 1; i <= numTopics; i++){
                    Scanner topicScanner = new Scanner(System.in);
                    System.out.println("Input a Topic "+i+":");
                    topicsArray[i-1] = topicScanner.nextLine()+",";
                }
                StringBuffer topics = new StringBuffer();
                for(int i = 0; i < topicsArray.length; i++) {
                    topics.append(topicsArray[i]);
                }
                String strTopics = topics.toString();
                Scanner scanner = new Scanner(System.in);
                System.out.println("Input a question title :");
                String questionTitle = scanner.nextLine();
                System.out.println("Input the question difficulty (0 to 5) :");
                Integer questionDifficulty = scanner.nextInt();
                Question question = new Question(questionTitle);
                question.setDifficulty(questionDifficulty);
                question.setTopics(strTopics);
                //Create
                dao.create(question);
                break;
            case 2:
                System.out.println("************************ List of Questions : ********************");
                List<Question> questionList1 = dao.readALl();
                for (Question question1:questionList1){
                    System.out.println("ID="+question1.getId()+". Question:"+question1.getQuestion()+". Difficulty="+question1.getDifficulty()+". Topics:"+question1.getTopics());
                }
                System.out.println("\n\n\n");
                break;
            case 3:
                System.out.println("Modify a question : ");
                //Update
                Scanner updateScanner = new Scanner(System.in);
                System.out.println("New title :");
                String newTitle = updateScanner.nextLine();
                System.out.println("New difficulty (0 to 5) :");
                Integer newDifficulty = updateScanner.nextInt();
                System.out.println("UPDATE : Input the question ID :");
                Integer updateID = updateScanner.nextInt();
                Question newQuestion = new Question(newTitle);
                newQuestion.setDifficulty(newDifficulty);
                dao.update(newQuestion, updateID);
                break;
            case 4:
                System.out.println(" XXXXXXXXXXXXXXXXX Deletion : XXXXXXXXXXXXXXXX");
                //Delete
                Scanner deleteScanner = new Scanner(System.in);
                Integer deleteID = 0;
                boolean repeat = false;
                do {
                    System.out.println("DELETION : Input the question ID :");
                    String str = ScanUserId.nextLine();
                    try
                    {
                        Integer.parseInt(str);
                        //System.out.println(str + " is a valid integer");
                        deleteID = Integer.parseInt(str);
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println(str + " is not a valid integer");
                        repeat = true;
                    }
                } while (repeat);
                dao.delete(deleteID);
                break;
            case 5:
                System.out.println("///////////////// Search /////////////////");
                //Search
                System.out.println("Give a Topic : ");
                Scanner scanTopic = new Scanner(System.in);
                String searchTopic = scanTopic.nextLine();
                List<Question> questionList = dao.readALl();
                List<Question> retrivedList = new ArrayList<>();
                for (Question question1:questionList){
                    String striTopics = question1.getTopics();
                    String[] arrTopics = striTopics.split(",");
                    for (int i = 0; i < arrTopics.length; i++){
                        if (arrTopics[i] == searchTopic){
                            retrivedList.add(question1);
                        }
                    }
                }
                for (Question question1:retrivedList){
                    System.out.println("ID="+question1.getId()+". Question:"+question1.getQuestion()+". Difficulty="+question1.getDifficulty()+". Topics:"+question1.getTopics()+"\n\n\n");
                }
                break;
            case 6:
                System.out.println("Adding MCQ Choices :");
                boolean repeti = false;
                Integer findId = null;
                Scanner ScanMcqChoice = new Scanner(System.in);
                dao.readALl();
                do {
                    System.out.println("Enter the question ID :");
                    String str = ScanMcqChoice.nextLine();
                    try
                    {
                        Integer.parseInt(str);
                        //System.out.println(str + " is a valid integer");
                        findId = Integer.parseInt(str);
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println(str + " is not a valid integer");
                        repeti = true;
                    }
                } while (repeti);
                Question question1 = dao.find(findId);
                if(question1.getQuestion() == ""){
                    break;
                }
                System.out.println("Enter the choice value :");
                String choice = ScanMcqChoice.nextLine();
                MCQChoice mcqChoice = new MCQChoice(choice);
                Integer valid = null;
                do {
                    System.out.println("Enter the choice validity, (1) for True and (0) for False :");
                    String str = ScanMcqChoice.nextLine();
                    try
                    {
                        Integer.parseInt(str);
                        //System.out.println(str + " is a valid integer");
                        valid = Integer.parseInt(str);
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println(str + " is not a valid integer");
                    }
                } while (valid != 0 && valid != 1);
                System.out.println("********** List of Quizzes: *********");
                quizDAO.readALl();
                boolean repe = false;
                Integer idQuiz = null;
                do {
                    System.out.println("Enter the Quiz ID :");
                    String str = ScanMcqChoice.nextLine();
                    try
                    {
                        Integer.parseInt(str);
                        //System.out.println(str + " is a valid integer");
                        idQuiz = Integer.parseInt(str);
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println(str + " is not a valid integer");
                        repe = true;
                    }
                } while (repe);
                Quiz quiz = quizDAO.find(idQuiz);
                mcqChoice.setQuiz(quiz);
                mcqChoice.setQuestion(question1);
                if (valid == 1){
                    mcqChoice.setValid(true);
                }else {
                    mcqChoice.setValid(false);
                }

                mcqChoiceDAO.create(mcqChoice);
                mcqChoiceDAO.readALl();
                break;
            case 7:
                System.out.println("Creation of Quiz : ");
                Scanner scanQuiz = new Scanner(System.in);
                System.out.println("Enter Quiz title :");
                String quizTitle = scanQuiz.nextLine();
                Quiz quiz1 = new Quiz(quizTitle);
                quizDAO.create(quiz1);
                quizDAO.readALl();
                break;
            case 8:
                System.out.println("Exporting into TXT file : ");
                exportTxt();
                break;
            case 9:
                System.out.println("Exporting into PDF file : ");
                exportPdf();
                break;
            case 10:
                studentPanel();
                break;
        }
        adminPanel();
    }

    /**
     *  Menu Panel Function For Student or Person taking the Quizzes
     * @throws Exception
     */
    public static void studentPanel() throws Exception {
        QuestionDAO dao = new QuestionDAO();
        MCQChoiceDAO mcqChoiceDAO = new MCQChoiceDAO();
        Scanner ScanUserId = new Scanner(System.in);
        Integer menuID = 0;
        do { // 4 Menus, if the User enter Bad value an exception is thrown
            System.out.println("Type Number to go to the menu");
            System.out.println(" 1) Take a Quiz evaluation");
            System.out.println(" 2) Export Quiz in TXT");
            System.out.println(" 3) Export Quiz in PDF");
            System.out.println(" 4) Admin Panel");
            String str = ScanUserId.nextLine();
            try
            {
                Integer.parseInt(str);
                //System.out.println(str + " is a valid integer");
                menuID = Integer.parseInt(str);
            }
            catch (NumberFormatException e)
            {
                System.out.println(str + " is not a valid integer");
            }
        } while (menuID != 1 && menuID != 2 && menuID !=3 && menuID !=4);
        switch (menuID) {
            case 1:
                groupChoiceByQuestion(); // Show Quiz
                List<Question> questionList = new ArrayList<>();
                questionList = dao.readALl();
                boolean rep = false;
                List<MCQChoice> mcqChoices = new ArrayList<>();
                int mcqId = 0;
                for (int i = 0; i < questionList.size(); i++){ // Loop over the Quizzes To give Answers depending on the Question Number
                    do {
                        System.out.println("Enter the ID of the Correct Answer for Question ("+ (i + 1 )+"):");
                        String str = ScanUserId.nextLine();
                        try
                        {
                            Integer.parseInt(str);
                            //System.out.println(str + " is a valid integer");
                            mcqId = Integer.parseInt(str);
                        }
                        catch (NumberFormatException e)
                        {
                            System.out.println(str + " is not a valid integer");
                            rep = true;
                        }
                    } while (rep);
                    MCQChoice mcqChoice = mcqChoiceDAO.find(mcqId);
                    if (mcqChoice.getChoice().equals("")){
                        break;
                    }
                    mcqChoices.add(mcqChoice);
                }
                if (mcqChoices.size() != 0){ // If there some answers we can make an evaluation
                    Double marks = 0.0;
                    for (MCQChoice mcqChoice:mcqChoices){
                        if (mcqChoice.isValid()){
                            marks++;
                        }
                    }
                    //Here we take Total of Good Answers divided By the Total of Questions, and Give the Percentage of Success!
                    Double totalMarks = (marks / questionList.size()) * 100; 
                    System.out.println("*********************************************************************************************");
                    System.out.println("********************    YOUR GRADE IN PERCENTAGE = "+totalMarks+"%    ******************************");
                    System.out.println("*********************************************************************************************");

                }
                break;
            case 2:
                System.out.println("Exporting into TXT file : ");
                exportTxt();
                break;
            case 3:
                System.out.println("Exporting into PDF file : ");
                exportPdf();
                break;
            case 4:
                adminPanel();
                break;
        }
        studentPanel();
    }
}
