/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(function () {

    "use strict";

 

    $(".select-level").on("change", function () {

        $(".select-subjects").empty().prop("disabled", false);

        $("<option/>")

            .text("Seleccione una asignatura")

            .appendTo(".select-subjects");

 

        var idLevel = $(this).val();

 

        if (!idLevel)

            return;

 

        $.getJSON("subjects.htm", { idPais: idLevel }, function (data) {

            $.each(data, function () {
                
                console.log( "JSON Data: " + json.users[ 3 ].name );
                var subjects = this;


                $(".select-subjects").empty().prop("disabled", true);       
                $("<option/>", { value: subjects.id })

                    .text(subjects.nombre)

                    .appendTo(".select-subjects");
                    

            });

 

            $(".select-subjects").trigger("change");

        });

    });

 

//    $(".select-provincia").on("change", function () {
//
//        $(".select-poblacion").empty();
//
//        $("<option/>")
//
//            .text("Seleccione una población")
//
//            .appendTo(".select-poblacion");
//
// 
//
//        var idProvincia = $(this).val();
//
// 
//
//        if (!idProvincia)
//
//            return;
//
// 
//
//        $.getJSON("/Poblaciones", { idProvincia: idProvincia }, function (data) {
//
//            $.each(data, function () {
//
//                var poblacion = this;
//
// 
//
//                $("<option/>", { value: poblacion.id })
//
//                .text(poblacion.nombre)
//
//                .appendTo(".select-poblacion");
//
//            });
//
//        });
//
//    });

});