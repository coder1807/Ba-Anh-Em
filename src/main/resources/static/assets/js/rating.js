window.onload = function() {

    // Hàm hiển thị thông báo
    function hienThiThongBao(text, duration, className) {
        Toastify({
            text,
            duration,
            className,
            close: false,
            gravity: "top",
            position: "left",
            stopOnFocus: true,
            style: {
                width: 600,
                height: 250,
            },
            backgroundColor: "red",
        }).showToast();
    }

    // rating
    function rate(value) {
        var filmId = document.getElementById('filmId').value;
        var content = document.getElementsByName('content')[0].value;

        fetch(`/film-details/${filmId}/rating`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                star: value,
                content: content
            }),
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Clear the textarea
                document.getElementsByName('content')[0].value = '';

                // Update the UI with the new rating
                const newRating = data.rating;
                const newRatingElement = document.createElement('div');
                newRatingElement.classList.add('movie-review-item');
                newRatingElement.innerHTML = `
                    <div class="flex-rating">
                        <div class="movie-review-info">
                            <div>
                                <span>${newRating.formattedDate}</span>
                            </div>
                            <h6 class="subtitle">
                                <a href="#0">${newRating.user.fullname}</a>
                            </h6>
                        </div>
                        <div class="movie-review-content">
                            <div class="review">
                                ${generateStars(newRating.star)}
                            </div>
                        </div>
                        <div style="margin-left: 85px">
                            <form action="/films/film-details/${filmId}/delete-rating/${newRating.id}" method="post" style="display: inline;">
                                <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc không ?')">
                                    <i class="fa-solid fa-trash"></i>
                                </button>
                            </form>
                        </div>
                    </div>
                    <p class="rating-content">${newRating.content}</p>
                `;
                document.querySelector('.movie-review-container').appendChild(newRatingElement);

                // Show success message
                hienThiThongBao("Đánh giá đã được thêm thành công", 2000, 'bg-success');
            } else {
                // Show error message
                hienThiThongBao(data.message, 3000, 'bg-danger');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            hienThiThongBao("Có lỗi xảy ra khi thêm đánh giá", 3000, 'bg-danger');
        });
    }
    // render star
    function generateStars(count) {
        let stars = '';
        for (let i = 0; i < count; i++) {
            stars += '<i class="flaticon-favorite-heart-button"></i> ';
        }
        return stars;
    }
}