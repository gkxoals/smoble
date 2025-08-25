import React, { useEffect, useState } from 'react';

function App() {
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetch('/api/main')
      .then(res => res.text())
      .then(data => setMessage(data));
  }, []);

  return (
    <div>
      <p>리액트를 시작을 해봅시다.</p>
      
    </div>
  );
}

export default App;