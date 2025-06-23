Quill.register('modules/autoLinks', quillAutoLinks.default);

// Quill 에디터 초기화
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
        },
        autoLinks: true  // 🔥 자동 링크 모듈 활성화
    }
});

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

// submit 시 content 안에 HTML 저장
function submitForm() {
    const content = document.querySelector('input[name=content]');
    content.value = quill.root.innerHTML;
}

document.querySelector('.board-form').addEventListener('submit', submitForm);
