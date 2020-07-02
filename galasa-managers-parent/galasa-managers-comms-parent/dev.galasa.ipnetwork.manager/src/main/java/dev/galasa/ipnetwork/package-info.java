/*
 * Licensed Materials - Property of IBM
 * 
 * (c) Copyright IBM Corp. 2020.
 */
/**
 * IP Network Manager
 * 
 * @galasa.manager IPNetwork 
 * 
 * @galasa.release.state Alpha
 * 
 * @galasa.description
 * 
 *  This Manager enables access to applications over TCP/IP internets. The 
 *  Manager interrogates the Configuration Properties Store (CPS) to get hostname                  
 *  and port information so that you do not need to hard-code these values into 
 *  your tests. 
 *  <br><br>
 *  The CPS defines object properties, so using this store to access hostnames and port 
 *  numbers means that the tester does not need to know the location of the application 
 *  to which they are connecting. 
 *  <br><br>
 *  The system administrator can set different properties in the CPS to allow a test to run 
 *  against multiple environments without changing the source code. 
 *  <br><br>
 *  
 *  You can view the <a href="https://javadoc.galasa.dev/dev/galasa/ipnetwork/package-summary.html"
 *  target="_blank" rel="noopener noreferrer">Javadoc documentation for the Manager here</a>. 
 *  <br><br>
 *                     
 */
package dev.galasa.ipnetwork;
