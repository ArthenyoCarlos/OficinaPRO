INSERT INTO tb_permission (id, created_at, updated_at, created_by, updated_by, name, description)
VALUES
    ('11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 'ROLE_ADMIN', 'Administrador do sistema'),
    ('22222222-2222-2222-2222-222222222222', CURRENT_TIMESTAMP, NULL, 'SYSTEM', NULL, 'ROLE_USER', 'Usuario padrao');

INSERT INTO tb_user (
    id,
    created_at,
    updated_at,
    created_by,
    updated_by,
    username,
    full_name,
    email,
    password,
    account_non_expired,
    account_non_locked,
    credentials_non_expired,
    enabled
)
VALUES (
    '33333333-3333-3333-3333-333333333333',
    CURRENT_TIMESTAMP,
    NULL,
    'SYSTEM',
    NULL,
    'admin',
    'Administrador',
    'admin@oficinapro.com',
    '{pbkdf2}d9173b001d4bd1fca992f2c6299d2d426a4d67c603b7a5101e82b0e8e4a67f12d70b1dafcb0b9ca6',
    TRUE,
    TRUE,
    TRUE,
    TRUE
);

INSERT INTO tb_user_permission (user_id, permission_id)
VALUES
    ('33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111'),
    ('33333333-3333-3333-3333-333333333333', '22222222-2222-2222-2222-222222222222');
