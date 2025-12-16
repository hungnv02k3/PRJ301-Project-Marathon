/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dal.AccountDAO;
import dal.RunnerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Account;
import models.Runner;

/**
 *
 * @author User
 */
public class SignUpController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account != null) {
            response.sendRedirect("login");
        } else {
            request.getRequestDispatcher("views/signup.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        AccountDAO accountDAO = new AccountDAO();
        RunnerDAO runnerDAO = new RunnerDAO();
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String gender = request.getParameter("gender");
        String dob = request.getParameter("dob");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        try {
            // check username
            if (accountDAO.getAccountByUsername(username) != null) {
                request.setAttribute("error", "Username already exists!");
                request.getRequestDispatcher("views/signup.jsp").forward(request, response);
                return;
            }

// ✅ check email runner
            if (runnerDAO.existsByEmail(email)) {
                request.setAttribute("error", "Email already registered!");
                request.getRequestDispatcher("views/signup.jsp").forward(request, response);
                return;
            }
            if (runnerDAO.existsByPhone(phone)) {
                request.setAttribute("error", "Phone already registered!");
                request.getRequestDispatcher("views/signup.jsp").forward(request, response);
                return;
            }
// tạo account
            Account acc = new Account(username, password, "runner");
            int accountId = accountDAO.insertAccount(acc);
            if (accountId == -1) {
                request.setAttribute("error", "Create account failed");
                request.getRequestDispatcher("views/signup.jsp").forward(request, response);
                return;
            }

// tạo runner
            Runner r = new Runner(
                    accountId,
                    fullName,
                    gender,
                    java.sql.Date.valueOf(dob),
                    email,
                    phone
            );

            runnerDAO.insertRunner(r);

            session.setAttribute("msg", "Register successfully!");
            response.sendRedirect("login");

        } catch (Exception ex) {
            request.setAttribute("error", "Create account failed");
            request.getRequestDispatcher("views/signup.jsp").forward(request, response);
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
