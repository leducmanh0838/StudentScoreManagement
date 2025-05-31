document.addEventListener('DOMContentLoaded', function () {
    const ctx = document.getElementById('pieChart').getContext('2d');

    // Lấy dữ liệu đã được truyền từ server (biến global)
    const labels = window.labelsData || [];
    const data = window.dataData || [];

    if (labels.length === 0 || data.length === 0) {
        console.warn('No data to render chart');
        return;
    }

    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                label: 'Số lượng sinh viên',
                data: data,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.6)',
                    'rgba(54, 162, 235, 0.6)',
                    'rgba(255, 206, 86, 0.6)',
                    'rgba(75, 192, 192, 0.6)'
                ],
                borderColor: 'white',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { position: 'right' },
                title: { display: true, text: 'Biểu đồ số sinh viên theo buổi học' }
            }
        }
    });
});
