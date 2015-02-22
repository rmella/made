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

        <script type="text/javascript">
            $(function() {
                MADEJS.Utils.ready(callback);
            });

            function callback() {
                var env = runDemo();
                
                $("#mapDimension").val(env.configuration.mapDimension);
                $("#mapFood").val(env.configuration.mapFood);
                $("#mapAgents").val(env.configuration.mapAgents);
                $("#mapVisibility").val(env.configuration.mapVisibility);
                $("#mapDays").val(env.configuration.mapDays);
        
                var agents = env.agents;
                var l = agents.length;
                for (var i = 0; i < l; i++) {
                    var $tr = $("#result tbody>tr:last");
                    var $clone = $tr.clone();
                    var children = $clone.children();
                    var agent = agents[i];

                    $(children[0]).html(agent.id);
                    $(children[1]).html(agent.name);
                    $(children[2]).html(agent.birth);
                    $(children[3]).html(agent.death);
                    $(children[4]).html(agent.mate);
                    $(children[5]).html(agent.father);
                    $(children[6]).html(agent.mother);
                    $(children[7]).html(agent.children);
                    $(children[8]).html(agent.archetypes);

                    $clone.insertAfter($tr);
                }
                $("#result tbody>tr:first").remove();
                $('.datatable').DataTable();
            }
            ;
        </script>

        <div id="content">
            <div class="centered">
                <h1>Demo Parameters</h1>
                <div class="info_table">
                    <div class="row">
                        <div class="name">
                            Map dimension
                        </div>
                        <div class="param">
                            <input id="mapDimension" type="text" disabled="disabled" />
                        </div>
                        <div class="name">
                            Food/day
                        </div>
                        <div class="param">
                            <input id="mapFood" type="text" disabled="disabled" />
                        </div>
                        <div class="name">
                            Initial agents
                        </div>
                        <div class="param">
                            <input id="mapAgents" type="text" disabled="disabled" />
                        </div>
                    </div>
                    <div class="row">
                        <div class="name">
                            Visibility (%)
                        </div>
                        <div class="param">
                            <input id="mapVisibility" type="text" disabled="disabled" />
                        </div>
                        <div class="name">
                            Virtual days
                        </div>
                        <div class="param">
                            <input id="mapDays" type="text" disabled="disabled" />
                        </div>
                        <div class="name"></div>
                        <div class="param"></div>
                    </div>
                </div>
                <div class="info_table">
                    <div class="row">
                        <div class="name">
                            Chromosome:
                        </div>
                    </div>
                    <div class="row">
                        <div class="param">
                            <textarea disabled="disabled"></textarea>
                        </div>
                    </div>
                </div>
                <div class="textblock right">
                    <a class="button" href="">Reload</a>
                </div>
                
                <h1>Backstories</h1>
                <table class="datatable" id="result">
                    <thead>
                        <tr>
                            <th>id</th>
                            <th>name</th>
                            <th>birth</th>
                            <th>death</th>
                            <th>mate</th>
                            <th>father</th>
                            <th>mother</th>
                            <th>children</th>
                            <th>archetypes</th>
                            <th>info</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>1</td>
                            <td>Cheddar</td>
                            <td>23</td>
                            <td>456</td>
                            <td>Maria Smell</td>
                            <td>unknown</td>
                            <td>unknown</td>
                            <td>3</td>
                            <td>4</td>
                            <td>info</td>
                        </tr>
                    </tbody>
                </table>

            </div>
        </div>
        <?php include 'footer.php'; ?>
    </body>
</html>

