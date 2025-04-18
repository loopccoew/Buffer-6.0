/**
 * Translation Service using LibreTranslate
 * To use the UI, visit: https://libretranslate.de/
 * 
 * Supported languages:
 * - en (English)
 * - es (Spanish)
 * - fr (French)
 * - de (German)
 * - it (Italian)
 * - pt (Portuguese)
 * - ru (Russian)
 * - ar (Arabic)
 * - zh (Chinese)
 * - ja (Japanese)
 */
export class TranslateService {
    private readonly API_URL = 'https://libretranslate.de/translate';

    /**
     * Translates text to the target language
     * @param text The text to translate
     * @param targetLanguage The target language code
     * @returns The translated text
     */
    async translateText(text: string, targetLanguage: string): Promise<string> {
        try {
            const response = await fetch(this.API_URL, {
                method: 'POST',
                body: JSON.stringify({
                    q: text,
                    source: 'auto',
                    target: targetLanguage,
                }),
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Translation failed');
            }

            const data = await response.json();
            return data.translatedText;
        } catch (error) {
            console.error('Translation error:', error);
            throw error;
        }
    }

    /**
     * Gets list of supported languages
     */
    async getSupportedLanguages(): Promise<any> {
        const response = await fetch('https://libretranslate.de/languages');
        return response.json();
    }
}
