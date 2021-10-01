/**
 * The Main Class of the Program
 */
package fr.epita.quizer.launcher;

import fr.epita.quizer.services.dao.*;
import java.util.Scanner;
import fr.epita.quizer.services.functions.Functions;

public class Launchme {

    public static void main(String[] args) throws Exception {

        //Data Access Objects
        QuizDAO quizDAO = new QuizDAO();
        QuestionDAO questionDAO = new QuestionDAO();
        StudentDAO studentDAO = new StudentDAO();
        QuestionDAO dao = new QuestionDAO();
        AnswerDAO answerDAO = new AnswerDAO();
        MCQAnswerDAO mcqAnswerDAO = new MCQAnswerDAO();
        MCQQuestionDAO mcqQuestionDAO = new MCQQuestionDAO();

        quizDAO.readALl();
        questionDAO.readALl();
        studentDAO.readALl();
        answerDAO.readALl();
        mcqAnswerDAO.readALl();
        mcqQuestionDAO.readALl();

        //UI
        System.out.println("\n***************************************************** EPITA **************************************************");
        System.out.println("******************************************** Welcome to Quiz Manager *****************************************");
        System.out.println("*********************************************** By Issaka SANFO **********************************************\n \n \n");
        Integer userID =0;
        Scanner userId = new Scanner(System.in);

        do { // Ask the User Profile, if the Entry is Bad thrown a Exception
            System.out.println("Enter in below, (1) for Student or (2) for Administrator :");
            String str = userId.nextLine();
            try
            {
                Integer.parseInt(str);
                //System.out.println(str + " is a valid integer");
                userID = Integer.parseInt(str);
            }
            catch (NumberFormatException e)
            {
                System.out.println(str + " is not a valid integer");
            }
        } while (userID != 1 && userID != 2);

        if(userID == 1){
            System.out.println("******************************************** Hello dear Student *****************************************");
            System.out.println("******* Menu ********");
            Functions.studentPanel();
        }else if (userID == 2){
            System.out.println("******************************************** Hello dear Admin *****************************************");
            System.out.println("******* Menu ********");
            Functions.adminPanel();
        }

    }

}
