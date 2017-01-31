/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//Añade los alumnos o los quita de la lessons          
$(function (){
    'use stric';
    $('#todoslosestudiantes').click(function() {
       $('.elemento-movil').appendTo('#sortable2');
    });
    $('#ningunestudiante').click(function() {
       $('.elemento-movil').appendTo('#sortable1'); 
    }); 
});


function conseguirAlumnos(){
    document.getElementById('misAlumnos').value = document.getElementById("sortable2").innerHTML;
}

/*function updateSelectOptions() {
    $.getJSON(lookupUrl,
        {searchId: $('#' + parentSelectElementId).val()}, 
        function(data) {
            var html = '<option value=""></option>';
            var len = data.length;
            for (var i = 0; i< len; i++) {
                html += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
            }

            $('#' + childSelectElementId).html(html);
        }
    );
}
//LLAMA A LESSONSBBDD.java levels para obtener las Subjects
/*$(function () {


'use strict';
$('.select-level').on('change', function () {
    var id = $(this).val();
    alert(id);
    if(id){
    $.ajax({
        url: 'dispatcher-servlet.xml',
        type: 'POST',
        dataType: 'id=' +id,
        success: function() {
            $('#comboSubject').html('<option value="">que pasa</option>');
            alert( "Exito" );
        }
    });
    }else{
        $('#comboSubject').html('<option value="">First choose a level</option>');
    }
});
});*/


//LLAMA A LESSONSBBDD.java filtra por clase a los estudiantes
$(function () {  
    'use strict';
    $('.select-level-students').on('change', function () { 
        $('#form').attr('action', 'lessons.htm?accion=' + $('#levelStudentSelecccionado').val());
        $('.btn').click();
    });
});

//LLAMA A LESSONSBBDD.java levels para obtener las Subjects
$(function () {
    'use strict';
    $('.select-level').on('change', function () {
        $('#form').attr('action', 'lessons.htm?accion=' + $('#comboSubject').val());
        $('.btn').click();
    });
});
//LLAMA A LESSONSBBDD.java comboSubsection para obtener las Subsection

$(function () {
    "use strict";
    $(".select-subjects").on("change", function () {
        $("#form").attr('action', 'lessons.htm?accion=' + $("#comboSubsection").val());
        $(".btn").click();
    });
});
//LLAMA A LESSONSBBDD.java comboEquipment para obtener el equipamiento

$(function () {

"use strict";
$(".select-subsection").on("change", function () {
    $("#form").attr('action', 'lessons.htm?accion=' + $("#comboEquipment").val());
    $(".btn").click();
    });
});

/*function checkear(){
    if(document.getElementById('titulo').value===''){
          return false;
    }
    if(document.getElementById('idlevel').value==='0'){
           return false;
    }	
    if(document.getElementById('idsubjects').value==='0'){
           return false;
    }	
    if(document.getElementById('idsubsection').value==='0'){
           return false;
    }	
    if(document.getElementById('datetimepickerinicio').value=== undefined){
           return false;
    }	
    if(document.getElementById('datetimepickerfin').value=== undefined){
           return false;
    }
    return true;
}*/
//ENVIA EL FORMULARIO PARA CREAR LA LECCION
/*$(function () {
    //var aux = "levels";
    $("#crearLessons").on("click", function () {
        //if(checkear()){
            document.getElementById('misAlumnos').value = document.getElementById("sortable2").innerHTML; 
            aux = $("#crearLessons").val();
        //}else{
            //window.alert = function () {
            //alertDGC("Hay que rellenar los campos");
            //};
            //alert("Hay que rellenar los campos");
        //}
    });
    $("#form").attr('action', 'lessons.htm?accion=' + $("#crearLessons").val());
});*/

/*$(function () {
    $("#form").attr('action', 'lessons.htm?accion=' + $("#crearLessons").val());
});*/

$(function () {
    "use strict";
    $(".btn-eliminar").on("click", function () {
        $("#form").attr('action', 'lessons.htm?accion=eliminar');
        $(".btn-eliminar").click();
    });
});

//Rellenar detalles lessons
$(function () {
    "use strict";
    $(".btn-detalles").on("click", function () {
        $("#form").attr('action', 'lessons.htm?accion=detalles') + $("#details").val();
        $(".btn-detalles").click();
    });
});

/*$(function () {
    "use strict";
    $(".btn-modificar").on("click", function () {
        $("#form").attr('action', 'lessons.htm?accion=blablabla') + $("#modificar").val();
        $(".btn-modificar").click();
    });
});*/

$(document).ready(function() {
    $('#adjuntarRecursos').click(function() {
        var $this = $(this);
    // $this will contain a reference to the checkbox   
        if ($this.is(':checked')) {
            $('#contenedorRecursos').show(); // the checkbox was checked
            $('#archivo').attr("disabled", false);
        } else {
            $('#contenedorRecursos').hide(); // the checkbox was unchecked
        }
    });    
});


$(function (){    
    var idLessonsDetails = $( "#details" ).val();
    if ( idLessonsDetails !== null){
        $('#botondetalles').attr('data-target','.bs-example-modal-lg');
        $('#botondetalles').click();
    }else{
        alert("mierda");    
    }

});