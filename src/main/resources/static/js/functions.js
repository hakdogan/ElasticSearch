function fillTable(){

    $('#myTable').find("tr:gt(0)").remove();
    $.ajax({
        type: 'GET',
        url: "/api/all",
        contentType: "application/json; charset=utf-8",
        success: function(result) {

            for (i = 0; i <= result.length - 1; i++) {
                var obj = result[i];
                $('#myTable > tbody:last').after('<tr><td>' + obj.firstname + '</td>'
                    + '<td>' + obj.lastname + '</td>'
                    + '<td>' + obj.message + '</td>'
                    + '<td><button type=\'button\' onclick=deleteDocument(\'' + obj.id + '\') class=\'btn btn-danger\'>Delete</button></td>');
            };
        },
        error: function(err) {
            console.log("AJAX error in request: " + JSON.stringify(err, null, 2));
            alert(JSON.stringify(err, null, 2));
        }
    });
}

function deleteDocument(id) {
    $.getJSON("/api/delete/", {id: id});
    alert('The document was deleted.');
    $(fillTable());
}

function clearFields() {
    $("#firstName").val('');
    $("#lastName").val('');
    $("#message").val('');
}

$(document).ready(function() {

    $(fillTable());

    $( "#query" ).keyup(function() {
        $.getJSON("/api/search/", {query: $('#query').val()} , function(result) {
            $('#myTable').find("tr:gt(0)").remove();
            for (i = 0; i <= result.length - 1; i++) {
                var obj = result[i];
                $('#myTable > tbody:last').after('<tr><td>' + obj.firstname + '</td>'
                    + '<td>' + obj.lastname + '</td>'
                    + '<td>' + obj.message + '</td>'
                    + '<td><button type=\'button\' onclick=deleteDocument(\'' + obj.id + '\') class=\'btn btn-danger\'>Delete</button></td></tr>');
            };
        });
    });

    $("#save").click(function(){
        event.preventDefault();

        var document = {
            id: null,
            firstname: $("#firstName").val(),
            lastname: $("#lastName").val(),
            message: $("#message").val()
        };

        $.ajax({
            type: 'POST',
            url: "/api/create",
            data: JSON.stringify(document),
            contentType: "application/json; charset=utf-8",
            success: function (id) {
                $('#myTable > tbody:last').after('<tr><td>' + $("#firstName").val() + '</td>'
                    + '<td>' + $("#lastName").val() + '</td>'
                    + '<td>' + $("#message").val() + '</td>'
                    + '<td><button type=\'button\' onclick=deleteDocument(\'' + id + '\') class=\'btn btn-danger\'>Delete</button></td></tr>');

                $(clearFields());
            },
            error: function(err) {
                console.log("AJAX error in request: " + JSON.stringify(err, null, 2));
                alert(JSON.stringify(err, null, 2));
            }
        });
    });
});
