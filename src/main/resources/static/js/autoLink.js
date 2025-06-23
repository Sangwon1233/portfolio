// autoLink.js
function enableAutoLink(quill) {
  quill.on('text-change', function (delta, oldDelta, source) {
    if (source !== 'user') return;

    const range = quill.getSelection();
    if (!range) return;

    const [line, offset] = quill.getLine(range.index);
    const text = line.domNode.innerText;

    const urlRegex = /(https?:\/\/[^\s]+|www\.[^\s]+)/g;
    let match;
    while ((match = urlRegex.exec(text)) !== null) {
      const url = match[0];
      const index = match.index;
      const fullUrl = url.startsWith('http') ? url : 'http://' + url;
      quill.formatText(line.offset(index), url.length, 'link', fullUrl, 'user');
    }
  });
}
