<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Registered_Loans extends Model
{
    use HasFactory;
    protected $table = 'registered_loans';

    protected $primaryKey = 'loan_application_number';
    public $incrementing = false;
}
