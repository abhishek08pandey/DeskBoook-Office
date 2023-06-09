<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Seat accepted mail to employee</title>
  </head>
  <body style="font-family: sans-serif;">
    <div style="max-width: 768px; margin: 0 auto; background-color: #fbfbfb; padding: 5px;">
      <table>
    	  <c:forEach var="body" items="${body}">
        <tr>
          <td>
            <p>Dear ${body.employeeName},</p>
          </td>
        </tr>
        <tr style="font-size: 14px;">
          <td>
            <p>
              We are pleased to inform you that your office seat request in the
              Deskbook Application System has been approved. Congratulations! Here
              are the details regarding your approved seat:
            </p>
          </td>
        </tr>
        <tr style="font-size: 14px;">
          <td>
            <p>Date: ${body.bookingDate}</p>
           <!-- <p>Location: ${body.city}</p>
            <p>Floor: ${body.floorName}</p>-->
            <p>Seat Number: ${body.seatNumber}</p>
            <p>Duration: ${body.duration}</p>
          </td>
        </tr>
        <tr>
        </tr>
        <tr>
          <td>
            <p>Congratulations on the approval of your office seat.</p>
            <p>Thanks,</p>
            <p>
              <img
                src="./Logo (1).png"
                alt="db-logo"
                style="
                  width: 30px;
                  border-right: 2px solid black;
                  padding-right: 4px;
                "
              />
              Deskbook
            </p>
          </td>
        </tr>
        </c:forEach>
      </table>
    </div>
  
  </body>
</html>
