function validateEmpty(element_id1, element_id2,button_id){
    var e1 = document.getElementById(element_id1).value;
    var e2 = 'dummy_string';
    if(element_id2 != "dummy"){
        var e2 = document.getElementById(element_id2).value;
    }

    if(element_id2 == "editprofile-phone"){
        var regex = /^\d{9,12}$/;
        if(!regex.test(e2)){
            document.getElementById(element_id2).style.backgroundColor = 'red';
            alert("Nomer telepon harus diisi angka dengan panjang 9-12 digit");
            return false;
        }
    }

    if(e1 == "" || e1 == null || e2 == "" || e2 == null ){
        alert("Kolom tidak boleh kosong");
        return false;
    }else{
        document.getElementById(button_id).click();
        return true;
    }
}

function changePath() {
    document.getElementById("editprofile-image-path").innerHTML = document.getElementById("editprofile-new-image").files[0].name;
}

function deleteLocation(row_id){
    if(confirm("Yakin mau dihapus?")==1){
        document.getElementById("edit-location-button-delete-" + row_id).click();
    }
}

function updateLocation(row_id){
    var location1 = document.getElementById("location1-" + row_id).value;
    document.getElementById("edit-profile-row-" + row_id).innerHTML = "<input type='text' name='locationDummy' id='edit-profile-input-dummy-"+ row_id +"' value='" + location1 + "'>";
    document.getElementById("edit-profile-icon-edit-" + row_id).src = "img/tick.svg";

    var save = document.getElementById("edit-profile-icon-edit-" + row_id);

    save.onclick = function () {
        var inputValue = document.getElementById("edit-profile-input-dummy-"+ row_id +"").value;
        if(inputValue == "" || inputValue == null){
            alert("Kolom tidak boleh kosong");
        }else{
            document.getElementById("location2-"+row_id).value = inputValue;
            document.getElementById("edit-location-button-update-" + row_id).click();
        }
    }
}

function clicked(e){
    if(!confirm('Are you sure you want to pick this driver?'))e.preventDefault();
}