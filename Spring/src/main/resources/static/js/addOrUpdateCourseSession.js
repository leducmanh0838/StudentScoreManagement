document.getElementById('courseName').addEventListener('input', function () {
    const input = this.value;
    const list = document.getElementById('coursesList').options;
    for (let i = 0; i < list.length; i++) {
        if (list[i].value === input) {
            document.getElementById('courseId').value = list[i].getAttribute('data-id');
            break;
        }
    }
});

document.getElementById('teacherName').addEventListener('input', function () {
    const input = this.value;
    const list = document.getElementById('teachersList').options;
    for (let i = 0; i < list.length; i++) {
        if (list[i].value === input) {
            document.getElementById('teacherId').value = list[i].getAttribute('data-id');
            break;
        }
    }
});