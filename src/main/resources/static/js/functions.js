function fillTable(){

    $("#myTable").find("tr:gt(0)").remove();
    $.ajax({
        type: "GET",
        url: "/api/all",
        contentType: "application/json; charset=utf-8",
        success: function(result) {
            result.forEach(function (obj) {
                $("#myTableBody").append("<tr><td>" + obj.title + "</td>"
                    + "<td>" + obj.title + "</td>"
                    + "<td>" + obj.content + "</td>"
                    + "<td><button type=\"button\" onclick=\"fillFields('" + obj.id + "\', '" + obj.title + "\', \'"
                    + obj.title + "\', \'" + obj.content + "\')\" class=\"btn btn-light\">Update</button>"
                    + " | <button type=\"button\" onclick=\"deleteDocument(\'" + obj.id + "\')\" class=\"btn btn-danger\">Delete</button></td>");
            });
        },
        error: function(err) {
            alert(JSON.stringify(err, null, 2));
        }
    });
}

function deleteDocument(id) {
    $.getJSON("/api/delete/", {id: id});
    alert("The document was deleted.");
    $(fillTable());
}

function fillFields(id, title, subject, content) {
    $("#docId").val(id);
    $("#subject").val(title);
    $("#subject").val(subject);
    $("#content").val(content);
}

function clearFields() {
    $("#docId").val("");
    $("#title").val("");
    $("#subject").val("");
    $("#content").val("");
    $("#query").val("");
}

$(document).ready(function() {

    $(fillTable());

    $( "#query" ).keyup(function() {
        $.getJSON("/api/search/", {query: $("#query").val()} , function(result) {
            $("#myTable").find("tr:gt(0)").remove();
            result.forEach(function (obj) {
                $("#myTable > tbody:last").after("<tr><td>" + obj.subject + "</td>"
                    + "<td>" + obj.title + "</td>"
                    + "<td>" + obj.content + "</td>"
                    + "<td><button type=\"button\" onclick=\"fillFields('" + obj.id + "\', '" + obj.subject + "\', \'"
                    + obj.title + "\', \'" + obj.content + "\')\" class=\"btn btn-light\">Update</button>"
                    + " | <button type=\"button\" onclick=\"deleteDocument(\'" + obj.id + "\')\" class=\"btn btn-danger\">Delete</button></td></tr>");
            });
        });
    });

    $("#save").click(function(){
        event.preventDefault();
        var id = "" === $("#docId").val()?null:$("#docId").val();
        var url = null === id?"/api/create":"/api/update";
        var document = {
            id: id,
            title: $("#title").val(),
            subject: $("#subject").val(),
            content: $("#content").val()
        };

        $.ajax({
            type: "POST",
            url: url,
            data: JSON.stringify(document),
            contentType: "application/json; charset=utf-8",
            success: function () {
                $(clearFields());
                $(fillTable());
            },
            error: function(err) {
                alert(JSON.stringify(err, null, 2));
            }
        });
    });
});