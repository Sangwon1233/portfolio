const quill = new Quill('#editor', {
    theme: 'snow',
    placeholder: '내용을 입력하세요...',
    modules: {
        toolbar: {
            container: [
                ['bold', 'italic', 'underline'],
                [{ 'header': 1 }, { 'header': 2 }],
                [{ 'list': 'ordered' }, { 'list': 'bullet' }],
                ['link', 'image']
            ],
            handlers: {
                image: imageHandler
            }
        }
    }
});

enableAutoLink(quill);

function imageHandler() {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = 'image/*';
    input.click();
    input.onchange = async () => {
        const file = input.files[0];
        if (file) {
            const formData = new FormData();
            formData.append('file', file);
            const response = await fetch('/api/image/upload', { method: 'POST', body: formData });
            const data = await response.json();
            const range = quill.getSelection();
            quill.insertEmbed(range.index, 'image', data.imageUrl);
        }
    };
}

document.querySelector('.board-form').addEventListener('submit', function (e) {
    e.preventDefault();  // 기본 submit 막기
    document.getElementById('content').value = quill.root.innerHTML;
    this.submit();  // 값 세팅한 후 수동으로 submit
});

