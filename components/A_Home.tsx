import React from 'react';
type MainProps = {
    title: string;
    content: string;
};

const MainContent = ({ title, content }: MainProps)=> {
    return(
        <article>
            <h2>{title}</h2>
            <p>{content}</p>
        </article>
    );

};

export default MainContent;