import { HTMLFather as HTML } from "../system_js/HTMLFather.js";
import { MainGrid } from "../system_js/mainGrid.js";
class App 
{
    static id;
    static name;
    static THEME;
    static isMath;
    static tools;
    static preparedToStart(data)
    {
        this.id = data.id;
        this.name = data.name;
        this.isMath = data.math;
        App.theme(data.theme);
        this.createGridPanel();
        for(let gr of data.grids)
        {
            new MainGrid(gr);
        }
    }
    static createGridPanel()
    {
        this.tools = HTML.createAndAppend("DIV");
        HTML.addStyles(["grid","fixed", "boxShadowCenter","opacityBlack", "leftSticky", "gapFivePix", "padding5px", "bRadius04rem", "zIndex500"], [this.tools]);
        let theme = HTML.createAndAppend("BUTTON", this.tools);
        theme.innerText = "Выбор темы";
        theme.onclick = ()=>{
            let popup = HTML.createPopup();
            let bDiv = HTML.createAndAppend("DIV", popup);
            HTML.addStyles(["grid", "gapFivePix"], [bDiv]);
            let green = HTML.createAndAppend("BUTTON", bDiv);
            green.innerText = "Зеленый";
            green.dataset.color = "green";
            green.onclick = this.changeTheme;
            let blue = HTML.createAndAppend("BUTTON", bDiv);
            blue.innerText = "Синий";
            blue.dataset.color = "blue";
            blue.onclick = this.changeTheme;
            let orang = HTML.createAndAppend("BUTTON", bDiv);
            orang.innerText = "Оранжевый";
            orang.dataset.color = "orange";
            orang.onclick = this.changeTheme;
            let old = HTML.createAndAppend("BUTTON", bDiv);
            old.innerText = "2021";
            old.dataset.color = "old";
            old.onclick = this.changeTheme;};
        let pages = HTML.createAndAppend("BUTTON", this.tools);
        pages.innerText = "Страницы";
        pages.onclick = ()=>{
            let popup = HTML.createPopup();
            popup.appendChild(MainGrid.deletePage());
            let count = HTML.createAndAppend("INPUT", popup);
            let store = {};
            let goButton = HTML.createAndAppend("BUTTON", popup);
            goButton.innerText = "Создать страницу";
            goButton.addEventListener("click", async ()=>{HTML.getValueAction(count, store, "count"); new MainGrid(await HTML.getJSON(["save_mg", App.id, store.count])); document.body.removeChild(popup.parentNode);});
        };
        let mathjax = HTML.createAndAppend("BUTTON", this.tools);
        mathjax.innerText = "Math";
        if(App.isMath)
        {
            mathjax.classList.add("bactive");
        }
        mathjax.onclick = (e)=>
        {
            this.isMath = this.isMath?false:true;
            e.target.classList.toggle("bactive");
            App.save();
        }
        let showJSON = HTML.createAndAppend("BUTTON", this.tools);
        showJSON.innerText = "Скачать ZIP";
        showJSON.onclick = ()=>
        {
           fetch(`/loadzip/${this.id}`).then(result => result.blob()).then(blob => {
            let b = URL.createObjectURL(blob);
            let a = HTML.create("a");
            a.href = b;
            a.download = `${this.name}.zip`;
            a.click();
           }).catch( exept => console.log(exept));
        }
    }
    static changeTheme(e)
    {
        let rootStyles = getComputedStyle(document.documentElement);
        App.theme(e.target.dataset.color);
        let changeThemeEvent = new CustomEvent("changeTheme", 
        { detail: { 
            main: rootStyles.getPropertyValue('--mainColor'),
            second: rootStyles.getPropertyValue('--secondColor')
        }});
        window.dispatchEvent(changeThemeEvent);
        App.save();
    }
    static theme(themeName)
    {
        document.documentElement.removeAttribute("theme");
        document.documentElement.setAttribute("theme", themeName);
        App.THEME = themeName;
    }
    static async save()
    {
        let saveData = {
            id: App.id,
            name: App.name,
            theme: App.THEME,
            math: App.isMath,
            grids: []  
        };
        await HTML.sendJSON(["save_page"], saveData);
    }
}
export {App}