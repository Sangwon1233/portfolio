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
        }
    }
});
// 커스텀 이미지 핸들러
function imageHandler() {
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('accept', 'image/*');
    input.click();
    input.onchange = async () => {
        const file = input.files[0];
        if (file) {
            const formData = new FormData();
            formData.append('file', file);
            try {
                const response = await fetch('/api/upload/image', {
                    method: 'POST',
                    body: formData
                });
                const data = await response.json();
                const imageUrl = data.url;
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

document.querySelector('form').addEventListener('submit', submitForm);
