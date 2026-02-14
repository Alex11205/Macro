import React from 'react';
import Signin from './A_Signin';
import { useState } from "react";

export default function Header({ username }) {
    return (
        <header>
            <h1>Welcome to The Macro Tracker</h1>
            <p>This is an tool to track macros in food</p>
            
            <h3 className='welcome'>Hello, {username}</h3>
            
        </header>

        // <h3>Hello</h3>
    );
};
