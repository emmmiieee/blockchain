package com.dronerecon.ws;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.security.SecureRandom;

/** * * Emi Cervantes */

public class DroneDataService extends HttpServlet{

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();

        // ##############################
        // 1. Get params passed in.
            // Get the following parameters from the request object and put them into strings:
            // area_id
            // tilex
            // tiley
            // totalcols
            // totalrows
            // r
            // g
            // ##############################
            String sAreaID = request.getParameter("area_id");
            String sTileX = request.getParameter("tilex");
            String sTileY = request.getParameter("tiley");
            String sColCount = request.getParameter("totalcols");
            String sRowCount = request.getParameter("totalrows");
            String sR = request.getParameter("r");
            String sG = request.getParameter("g");


            // Call cloud and pass data to be stored in DB
        try {
            // Call PortalDBService
            URL url = new URL(
                    "http://127.0.0.1:8080/dronereconportal/PortalDBService?area_id=" + sAreaID +"&tilex=" +sTileX
                            + "&tiley="+ sTileY +"&r=" + sR + "&g=" + sG);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }


            // ##############################
            // 2. Default value of beginning direction.

            // Set a string called sDirection to "right".
            // ##############################
            String sDirection = "right";



            // ##############################
            // 3. Calculate next drone move.

            // Determine next tile to move to.
            // Base this on current x and y.
            // Change sDirection if necessary.
            // Drone must serpentine from top left of grid back and forth down.
            // If rows are done, change sDirection to "stop".
            // ##############################
            int iTileX= Integer.parseInt(sTileX);
            int iTileY = Integer.parseInt(sTileY);
            int iTotalCol = Integer.parseInt(sColCount);
            int iTotalRow = Integer.parseInt(sRowCount);

            // Check if on even row
            if(iTileY%2==0){

                // Check if drone on last column, then increase y
                if(iTileX==iTotalCol-1){
                    iTileY++;
                    sDirection = "left";
                    sTileY=String.valueOf(iTileY);
                }

                //Drone is not on last column, so adjust x
                else {
                    iTileX++;
                    sDirection = "right";
                    sTileX=String.valueOf(iTileX);
                }

            }

            // Check if on odd row
            else {

                // Check if drone on far left column, then increase y
                if(iTileX==0){
                    iTileY++;
                    sDirection = "right";
                    sTileY=String.valueOf(iTileY);
                }
                // Drone is not on the far left column, then decrease x
                else {
                    iTileX--;
                    sDirection = "left";
                    sTileX=String.valueOf(iTileX);
                }
            }

            if (iTileY == iTotalRow){
                // Stop
                sDirection = "stop";
            }


            // ##############################
            // 4. Format & Return JSON string to caller.

        /* Return via out.println() a JSON string like this:
        {"area_id":"[area id from above]", "nextTileX":"[next tile x]", "nextTileY":"[next tile y]", "direction":"[direction string from above]"}
        */
            // ##############################
            String sReturnJson = "{\"area_id\":\"" + sAreaID + "\", \"nextTileX\":\"" + sTileX + "\", \"nextTileY\":\""
                    + sTileY + "\", \"direction\":\"" + sDirection + "\"}";
            out.println(sReturnJson);
        }
    }

