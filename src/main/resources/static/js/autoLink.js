function enableAutoLink(quill) {
    const urlRegex = /(https?:\/\/[^\s]+|www\.[^\s]+)/g;
    quill.root.addEventListener('keyup', function () {
        const sel = quill.getSelection();
        if (!sel || sel.length) return;
        const [leaf] = quill.getLeaf(sel.index);
        const text = leaf.text;
        const match = urlRegex.exec(text);
        if (match) {
            const url = match[0];
            const index = sel.index - url.length;
            quill.formatText(index, url.length, 'link', url.startsWith('http') ? url : 'http://' + url);
        }
    });
}
