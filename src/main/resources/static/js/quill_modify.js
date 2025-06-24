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

// 초기 데이터 삽입
const initialContentValue = document.getElementById('initialContent').value;
quill.root.innerHTML = initialContentValue;

// 이미지 핸들러
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
            try {
                const response = await fetch('/api/image/upload', {
                    method: 'POST',
                    body: formData
                });
                if (!response.ok) throw new Error('Upload failed');
                const data = await response.json();
                const imageUrl = data.imageUrl;
                const range = quill.getSelection();
                quill.insertEmbed(range.index, 'image', imageUrl);
            } catch (err) {
                alert('이미지 업로드 실패');
            }
        }
    };
}

document.querySelector('.board-form').addEventListener('submit', function () {
    document.getElementById('content').value = quill.root.innerHTML;
});
