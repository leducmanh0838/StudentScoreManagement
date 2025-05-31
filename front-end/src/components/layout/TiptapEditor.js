import React, { useEffect } from 'react';
import { EditorContent, useEditor } from '@tiptap/react';
import StarterKit from '@tiptap/starter-kit';
import './tiptap.css'; // optional, for basic styling

const TiptapEditor = ({ content, onChange }) => {
  const editor = useEditor({
    extensions: [StarterKit],
    content: content || '',
    onUpdate({ editor }) {
      onChange(editor.getHTML());
    },
  });

  useEffect(() => {
    if (editor && content !== editor.getHTML()) {
      editor.commands.setContent(content || '');
    }
  }, [content]);

  return <EditorContent editor={editor} />;
};

export default TiptapEditor;
