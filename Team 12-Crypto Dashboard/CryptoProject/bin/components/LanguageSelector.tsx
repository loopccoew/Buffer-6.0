import React, { useState, useEffect } from 'react';
import { TranslateService } from '../services/GoogleTranslateService';

const LanguageSelector = ({ onLanguageChange }) => {
    const [languages, setLanguages] = useState([]);
    const [selectedLang, setSelectedLang] = useState('en');

    useEffect(() => {
        const loadLanguages = async () => {
            const translator = new TranslateService();
            const langs = await translator.getSupportedLanguages();
            setLanguages(langs);
        };
        loadLanguages();
    }, []);

    return (
        <select
            value={selectedLang}
            onChange={(e) => {
                setSelectedLang(e.target.value);
                onLanguageChange(e.target.value);
            }}
            className="px-2 py-1 rounded border"
        >
            {languages.map((lang: any) => (
                <option key={lang.code} value={lang.code}>
                    {lang.name}
                </option>
            ))}
        </select>
    );
};

export default LanguageSelector;
