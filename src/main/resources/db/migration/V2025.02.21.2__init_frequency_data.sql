insert into remind_you.frequency (id, frequency_name, frequency_code, frequency_desc, frequency_number, frequency_cycle, cycle_unit, type, status, source, level, tenant_id, create_id, create_name, create_time, update_id, update_name, update_time, delete_flag)
values  (1887042190151270401, '每日一次', 'qd', '每日指定时间服用一次', 1, 1, 'DAY', 'UNKNOWN', true, 'system', 0, 1234, 1, 'admin', '2025-02-05 15:34:52', 1, 'admin', '2025-02-05 15:55:40', false),
        (1887049265166184449, '每日两次', 'bid', '每日指定时间服用两次', 2, 1, 'DAY', 'UNKNOWN', true, 'system', 0, 1234, 1, 'admin', '2025-02-05 16:02:59', 1, 'admin', '2025-02-05 16:13:21', false),
        (1887312708300607489, '隔日一次', 'qod', '每隔一天服用一次', 1, 2, 'DAY', 'UNKNOWN', false, 'system', 0, 1234, 1, 'admin', '2025-02-06 09:29:49', null, null, null, false),
        (1887313142453014530, '每周一次', 'qw', '每周服用一次', 1, 1, 'WEEK', 'NATURAL_WEEK', false, 'system', 0, 1234, 1, 'admin', '2025-02-06 09:31:32', null, null, null, false),
        (1887313325932843009, '每两周一次', '2w', '每两周服用一次', 1, 2, 'WEEK', 'NATURAL_WEEK', false, 'system', 0, 1234, 1, 'admin', '2025-02-06 09:32:16', null, null, null, false),
        (1887313574395023362, '三小时一次', 'q3h', '每三小时服用一次', 1, 3, 'HOUR', 'UNKNOWN', false, 'system', 0, 1234, 1, 'admin', '2025-02-06 09:33:15', null, null, null, false),
        (1887314717204451330, '四小时两次', 'q4h2', '每四小时服用两次', 2, 4, 'HOUR', 'UNKNOWN', false, 'system', 0, 1234, 1, 'admin', '2025-02-06 09:37:48', null, null, null, false);

insert into remind_you.frequency_detail (id, frequency_id, frequency_weekday, frequency_time, before_rule_time, after_rule_time, tenant_id, create_id, create_name, create_time, update_id, update_name, update_time, delete_flag)
values  (1887056131464122369, 1887049265166184449, 0, '08:30:30', '08:00:00', '09:00:00', 1234, 1, 'admin', '2025-02-05 16:30:16', null, null, null, false),
        (1887056202675015682, 1887049265166184449, 0, '18:30:30', '18:00:00', '19:00:00', 1234, 1, 'admin', '2025-02-05 16:30:33', null, null, null, false),
        (1888861551863689218, 1887312708300607489, 0, '18:30:30', '10:00:00', '19:00:00', 1234, 1, 'admin', '2025-02-10 16:04:22', 1, 'admin', '2025-02-10 16:10:42', false);