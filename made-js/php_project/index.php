<?php 
/*
 @licstart  The following is the entire license notice for the 
 JavaScript code in this page.
 
 Copyright (C) 2014  Rubén Héctor García Ortega
 
 The JavaScript code in this page is free software: you can
 redistribute it and/or modify it under the terms of the GNU
 General Public License (GNU GPL) as published by the Free Software
 Foundation, either version 3 of the License, or (at your option)
 any later version.  The code is distributed WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU GPL for more details.
 
 As additional permission under GNU GPL version 3 section 7, you
 may distribute non-source (e.g., minimized or compacted) forms of
 that code without the copy of the GNU GPL normally required by
 section 4, provided you include this license notice and a URL
 through which recipients can access the Corresponding Source.   
 
 
 @licend  The above is the entire license notice
 for the JavaScript code in this page.
 * */
?>

<html>
    <head>
        <?php include 'head.php'; ?>
    </head>
    <body>
        <?php include 'header.php'; ?>
        <div id="content">
            <div class="centered">
                <div class="textblock">
                    MADE-js is a javascript implementation of MADE.
                </div>
                <div class="columns2">
                    <div class="row">
                        <div class="column">
                            <h1>Just take me to the demo</h1>
                            <div class="textblock">
                                Yeah, straight to your goal... you will see the results of executing
                                a good chromosome to obtain the backstories... 
                            </div>
                            <div class="textblock right">
                                <a class="button" href="demo.php">Demoooo!!!!! &gt;&gt;</a>
                            </div>
                        </div>
                        <div class="column">
                            <h1>Try my own chromosome</h1>
                            <div class="textblock">
                                The backstories are randomly generated and they are guided by
                                a chromosome (that is, the personality of the agents).
                            </div>
    <!--                        <textarea placeholder="0,1; 0,34; ..." ></textarea>-->
                            <div class="textblock right">
                                <a class="button" href="chromosome.php">Test a chromosome &gt;&gt;</a>
                            </div>
                        </div>
                    </div>
                    <div class="separator"></div>
                    <div class="row">
                        <div class="column">
                            <h1>Obtain the best seed</h1>
                            <div class="textblock">
                                A Genetic algorithm will optimize the chromosome by using as a
                                fitness the archetypes that have emerged in the generated plot.
                            </div>
                            <div class="textblock right">
                                <a class="button" href="ga.php">Lets do science &gt;&gt;</a>
                            </div>
                        </div>
                        <div class="column">
                            <h1>I want to help</h1>
                            <div class="textblock">
                                Yes!! MADE needs some human guided fitness. You are sooooo kind! (Please, be honest)
                            </div>
    <!--                        <textarea placeholder="0,1; 0,34; ..." ></textarea>-->
                            <div class="textblock right">
                                <a class="button" href="humanguided.php">Human guided fitness &gt;&gt;</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <?php include 'footer.php'; ?>
    </body>
</html>

