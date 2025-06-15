import { useState } from "react";
import { register, checkDuplicateId } from "../api/auth";
import { useNavigate } from "react-router-dom";
import '../styles/FormCommon.css';
import './RegisterPage.css';

export default function RegisterPage() {
    const [accountId, setAccountId] = useState("");
    const [password, setPassword] = useState("");
    const [duplicateChecked, setDuplicateChecked] = useState(false);
    const [isDuplicate, setIsDuplicate] = useState<boolean | null>(null);

    const navigate = useNavigate();

    const handleCheckDuplicate = async () => {
        try {
            const response = await checkDuplicateId(accountId);
            const isDuplicate = response.data;

            setIsDuplicate(isDuplicate);
            setDuplicateChecked(true);

        } catch (e) {
            alert("중복 확인 실패");
            console.error(e);
        }
    };

    const handleRegister = async () => {
        if (isDuplicate) {
            alert("이미 사용 중인 아이디입니다");
            return;
        }
        try {
            await register(accountId, password);
            alert("회원가입 성공");
            navigate("/login");
        } catch {
            alert("회원가입 실패");
        }
    };

    return (
        <div className="form-container">
            <div className="form-group">
                <input

                    value={accountId}
                    onChange={(e) => {
                        setAccountId(e.target.value);
                        setDuplicateChecked(false); // 아이디 바꾸면 다시 확인해야 함
                    }}
                    placeholder="아이디"
                />
                <button onClick={handleCheckDuplicate} style={{ marginLeft: "8px" }}>
                    중복 확인
                </button>
            </div>
            {duplicateChecked && isDuplicate === false && <div className="feedback" style={{ color: "green" }}>사용 가능한 아이디입니다.</div>}
            {duplicateChecked && isDuplicate === true && <div className="feedback" style={{ color: "red" }}>이미 사용 중인 아이디입니다.</div>}

            <input
                className="input-block"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                type="password"
                placeholder="비밀번호"
            />
            <button className="submit-button" onClick={handleRegister}>
                회원가입
            </button>
        </div>
    );
}
