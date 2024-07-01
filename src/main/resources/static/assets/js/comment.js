window.onload = function() {
    // Thêm AJAX cho việc gửi bình luận
    const commentForm = document.querySelector('.blog-form');
    if (commentForm) {
        commentForm.addEventListener('submit', function(event) {
            event.preventDefault();

            const formData = new FormData(commentForm);
            const blogId = formData.get('blogId');
            const content = formData.get('content');

            // Tạo object chứa dữ liệu bình luận
            const commentData = {
                blogId: blogId,
                content: content
            };

            fetch(`/blog/blog-details/${blogId}/comment`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(commentData),
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Xóa nội dung trong textarea
                    commentForm.querySelector('textarea[name="content"]').value = '';

                    // Cập nhật phần hiển thị bình luận
                    const newComment = document.createElement('li');
                    newComment.innerHTML = `
                        <div class="blog-thumb-info">
                            <span>${data.comment.formattedDate}</span>
                            <h6 class="title">
                                <a href="#0">${data.comment.user.fullname}</a>
                            </h6>
                        </div>
                        <div class="blog-content">
                            <p>${data.comment.content}</p>
                        </div>
                        <div sec:authorize="hasAuthority('admin')">
                            <form th:action="@{/blog/blog-details/${blogId}/delete/${data.comment.id}}" method="post" style="display: inline;">
                                <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc không ?')">
                                    <i class="fa-solid fa-trash"></i>
                                </button>
                            </form>
                        </div>
                    `;
                    document.querySelector('.comment-area').appendChild(newComment);

                    // Ẩn thông báo lỗi nếu có
                    const errorMessageElement = document.querySelector('.error-message');
                    if (errorMessageElement) {
                        errorMessageElement.style.display = 'none';
                    }

                    // Hiển thị thông báo thành công
                    hienThiThongBao("Comment đã được thêm thành công", 2000, 'bg-success');
                } else {
                    // Hiển thị thông báo lỗi
                    const errorMessageElement = document.querySelector('.error-message');
                    if (errorMessageElement) {
                        errorMessageElement.textContent = data.message;
                        errorMessageElement.style.display = 'block';
                    }
                    hienThiThongBao(data.message, 3000, 'bg-danger');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                hienThiThongBao("Có lỗi xảy ra khi thêm comment", 3000, 'bg-danger');
            });
        });
    }
};

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